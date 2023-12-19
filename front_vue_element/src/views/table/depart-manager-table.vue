<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.title"
        placeholder="键入想要搜索的内容"
        style="width: 200px"
        class="filter-item"
        @keyup.enter.native="handleSearch"
        clearable
        @clear="handleClear"
      />
      <el-button
        style="margin-left: 10px"
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleSearch"
      >
        搜索
      </el-button>
      <el-button
        v-waves
        :loading="downloadLoading"
        class="filter-item"
        type="primary"
        icon="el-icon-download"
        @click="handleDownload"
      >
        导出
      </el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
      @sort-change="sortChange"
    >
      <el-table-column
        label="部门ID"
        prop="departID"
        sortable
        align="center"
        width="100"
      >
        <template slot-scope="{ row }">
          <span>{{ row.departID }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="部门名称"
        min-width="130"
        align="center"
        prop="departName"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.departName }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="员工ID"
        prop="id"
        sortable
        align="center"
        width="100"
      >
        <template slot-scope="{ row }">
          <span>{{ row.empID }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="员工姓名"
        min-width="130"
        align="center"
        prop="empName"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.empName }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="30%"
        style="width: 80%; margin-left: 10%"
      >
        <el-form-item label="员工ID" prop="empID">
          <el-input
            :disabled="dialogStatus === 'update'"
            v-model="temp.empID"
          />
        </el-form-item>

        <el-form-item
          label="每日考勤日期"
          prop="checkDate"
          v-if="this.$store.state.user.right == 1"
        >
          <el-date-picker
            :disabled="dialogStatus === 'update'"
            v-model="temp.checkDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item
          label="加班小时数"
          v-if="this.$store.state.user.right == 1"
          prop="overtimeHour"
        >
          <el-input v-model="temp.overtimeHour" />
        </el-form-item>

        <el-form-item
          label="是否上班"
          v-if="this.$store.state.user.right == 1"
          prop="workDay"
        >
          <el-select v-model="temp.workDay" placeholder="请选择是否上班">
            <el-option label="1" value="1"></el-option>
            <el-option label="0" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          label="是否出差"
          v-if="this.$store.state.user.right == 1"
          prop="businessTripDay"
        >
          <el-select
            v-model="temp.businessTripDay"
            placeholder="请选择是否出差"
          >
            <el-option label="1" value="1"></el-option>
            <el-option label="0" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          label="是否旷工"
          v-if="this.$store.state.user.right == 1"
          prop="absenceDay"
        >
          <el-select v-model="temp.absenceDay" placeholder="请选择是否旷工">
            <el-option label="1" value="1"></el-option>
            <el-option label="0" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          label="迟到次数"
          v-if="dialogStatus === 'create' && this.$store.state.user.right == 1"
          prop="lateDay"
        >
          <el-input v-model="temp.lateDay" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false"> 取消 </el-button>
        <el-button
          type="primary"
          @click="dialogStatus === 'create' ? createData() : updateData()"
        >
          提交
        </el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="直接上传员工信息表格"
      :visible.sync="excelDialogVisible"
      width="70%"
      center
    >
      <div class="app-container" style="overflow: hidden">
        <upload-excel-component
          :on-success="handleSuccess"
          :before-upload="beforeUpload"
        />
        <el-table
          :data="tableData"
          border
          highlight-current-row
          style="width: 100%; margin-top: 20px; overflow: auto"
          max-height="30vh"
        >
          <el-table-column
            v-for="item of tableHeader"
            :key="item"
            :prop="item"
            :label="item"
          />
        </el-table>
        <el-button
          style="margin-top: 20px; margin-left: 10px; float: right"
          type="plain"
          @click="downLoadStaticExcel"
          >下载模板</el-button
        ><el-button
          style="margin-top: 20px; float: right"
          type="primary"
          @click="uploadExcel"
          >上传表格</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
    
    <script>
import {
  getEmpList,
  addEmp,
  deleteEmp,
  updateEmp,
  addEmpList,
  getDailyCheckList,
  getDepartManagerList,
} from "@/api/emp";
import waves from "@/directive/waves"; // waves directive
import { parseTime } from "@/utils";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { get, set } from "js-cookie";
import UploadExcelComponent from "@/components/UploadExcel/index.vue";
import { getOneEmp, updateOneEmp, getOneCheckStat } from "@/api/normal";

const calendarTypeOptions = [
  { key: "CN", display_name: "China" },
  { key: "US", display_name: "USA" },
  { key: "JP", display_name: "Japan" },
  { key: "EU", display_name: "Eurozone" },
];

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name;
  return acc;
}, {});

export default {
  name: "ComplexTable",
  components: { Pagination, UploadExcelComponent },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: "success",
        draft: "info",
        deleted: "danger",
      };
      return statusMap[status];
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type];
    },
  },
  data() {
    return {
      tableData: [],
      tableHeader: [],
      file: null,
      tableKey: 0,
      list: null,
      allData: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: "+id",
      },
      calendarTypeOptions,
      temp: {
        remark: "",
        title: "",
        type: "",
        status: "published",
        empID: "",
        checkDate: "",
        overtimeHour: 0,
        workDay: 0,
        businessTripDay: 0,
        absenceDay: 0,
        lateDay: 0,
      },
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑员工信息",
        create: "增加员工信息",
      },
      rules: {
        empID: [{ required: true, message: "员工ID不能为空", trigger: "blur" }],
        checkDate: [
          { required: true, message: "日期不能为空", trigger: "blur" },
        ],
        overtimeHour: [
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        workDay: [{ required: true, message: "不能为空", trigger: "blur" }],
        businessTripDay: [
          { required: true, message: "电话不能为空", trigger: "blur" },
        ],
        absenceDay: [{ required: true, message: "不能为空", trigger: "blur" }],
        lateDay: [{ required: true, message: "不能为空", trigger: "blur" }],
      },
      downloadLoading: false,
      excelDialogVisible: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.listLoading = true;
      getDepartManagerList().then((response) => {
        this.list = response.data;
        this.allData = response.data;
        this.total = this.list.length;

        this.list = this.list.slice(
          (this.listQuery.page - 1) * this.listQuery.limit,
          this.listQuery.page * this.listQuery.limit
        );
        setTimeout(() => {
          this.listLoading = false;
        }, 1.5 * 1000);
      });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: "操作Success",
        type: "success",
      });
      row.status = status;
    },
    sortChange(data) {
      const { prop, order } = data;
      if (prop === "empName") {
        console.log("sort by empName");
        this.sortByZH(order).then(() => {
          setTimeout(() => {
            this.listLoading = false;
          }, 1.5 * 1000);
        });
      } else {
        setTimeout(() => {
          this.listLoading = false;
        }, 1.5 * 1000);
      }
    },
    sortByZH(order) {
      if (order === "ascending") {
        // this.listQuery.sort = "+id";
        this.listQuery.sort = "+empName";
        this.list.sort((a, b) => a.empName.localeCompare(b.empName));
      } else {
        // this.listQuery.sort = "-id";
        this.listQuery.sort = "-empName";
        this.list.sort((b, a) => a.empName.localeCompare(b.empName));
      }
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: "",
        timestamp: new Date(),
        title: "",
        status: "published",
        type: "",
      };
    },
    handleCreate() {
      this.resetTemp();
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    createData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          addEmp(this.temp).then(() => {
            this.list.unshift(this.temp);
            this.dialogFormVisible = false;
            this.$notify({
              title: "Success",
              message: "Created Successfully",
              type: "success",
              duration: 2000,
            });
            this.getList();
          });
        }
      });
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row); // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp);
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    updateData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp);
          if (this.$store.state.user.right == 0) {
            updateOneEmp(tempData).then((response) => {
              this.dialogFormVisible = false;
              this.$notify({
                title: "Success",
                message: response.message,
                type: "success",
                duration: 2000,
              });
              this.getList();
            });
          } else {
            updateEmp(tempData).then((response) => {
              this.dialogFormVisible = false;
              this.$notify({
                title: "Success",
                message: response.message,
                type: "success",
                duration: 2000,
              });
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      deleteEmp(row.empID).then((response) => {
        this.$notify({
          title: "Success",
          message: response.message,
          type: "success",
          duration: 2000,
        });
        this.getList();
      });
    },
    handleDownload() {
      this.downloadLoading = true;
      import("@/vendor/Export2Excel").then((excel) => {
        const tHeader = ["部门ID", "部门名称", "员工ID", "员工姓名"];
        const filterVal = ["departID", "departName", "empID", "empName"];

        const data = this.formatJson(filterVal);

        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: "部门表",
        });
        this.downloadLoading = false;
      });
    },
    formatJson(filterVal) {
      return this.allData.map((v) =>
        filterVal.map((j) => {
          if (j === "timestamp") {
            return parseTime(v[j]);
          } else {
            return v[j];
          }
        })
      );
    },
    handleSearch() {
      this.listLoading = true;
      let newList = [];
      for (let emp of this.allData) {
        var keys = Object.keys(emp);
        for (let key of keys) {
          if (String(emp[key]).includes(this.listQuery.title)) {
            newList.push(emp);
            break;
          }
        }
      }
      this.list = newList;
      this.downloadLoading = false;
      this.total = this.list.length;

      this.list = this.list.slice(
        (this.listQuery.page - 1) * this.listQuery.limit,
        this.listQuery.page * this.listQuery.limit
      );
      setTimeout(() => {
        this.listLoading = false;
      }, 1.5 * 1000);
    },
    handleClear() {
      this.listQuery.title = "";
      this.getList();
    },
    handleExcelUpload() {
      this.excelDialogVisible = true;
    },
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1;

      if (isLt1M) {
        return true;
      }

      this.$message({
        message: "文件小于1mb才能上传",
        type: "warning",
      });
      return false;
    },
    handleSuccess({ results, header }) {
      this.tableData = results;
      this.tableHeader = header;
    },
    uploadExcel() {
      let empList = [];
      for (let data of this.tableData) {
        let emp = {};
        let keys = Object.keys(data);
        emp["departID"] = data[keys[0]];
        emp["departName"] = data[keys[1]];
        emp["empID"] = data[keys[2]];
        emp["empName"] = data[keys[3]];
        if (
          emp["departID"] === undefined ||
          emp["departName"] === undefined ||
          emp["empID"] === undefined ||
          emp["empName"] === undefined
        ) {
          this.$notify({
            title: "Error",
            message: "请检查数据是否完整",
            type: "error",
            duration: 2000,
          });
          return;
        }
        empList.push(emp);
      }
      addEmpList(empList).then((response) => {
        this.$notify({
          title: "Success",
          message: response.message,
          type: "success",
          duration: 2000,
        });
        this.excelDialogVisible = false;
        this.tableData = [];
        this.tableHeader = [];
        this.getList();
      });
    },
    downLoadStaticExcel() {
      let a = document.createElement("a"); //创建一个<a></a>标签
      a.href = "/static/员工考勤表模板.xlsx"; // 给a标签的href属性值加上地址，注意，这里是绝对路径，不用加 点.
      a.download = "员工考勤表模板.xlsx"; //设置下载文件文件名，这里加上.xlsx指定文件类型，pdf文件就指定.fpd即可
      a.style.display = "none"; // 障眼法藏起来a标签
      document.body.appendChild(a); // 将a标签追加到文档对象中
      a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
      a.remove(); // 一次性的，用完就删除a标签
    },
  },
};
</script>
    