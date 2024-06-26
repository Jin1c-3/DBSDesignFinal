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
        label="员工ID"
        prop="id"
        sortable
        align="center"
        width="90"
        fixed
      >
        <template slot-scope="{ row }">
          <el-tag type="danger">{{ row.empID }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="工资月份"
        min-width="120"
        align="center"
        prop="wageDate"
        sortable
        fixed
      >
        <template slot-scope="{ row }">
          <el-tag type="danger">{{
            row.wageDate | parseTime("{y}-{m}")
          }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="基本工资"
        min-width="110"
        align="center"
        prop="baseWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.baseWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="日薪合计"
        width="110"
        align="center"
        prop="dayWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.dayWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="加班工资"
        width="110"
        align="center"
        prop="overtimeWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.overtimeWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="工龄工资"
        min-width="110"
        align="center"
        prop="seniorityWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.seniorityWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="出差补贴"
        min-width="110"
        align="center"
        prop="businessTripWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.businessTripWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="旷工扣款"
        min-width="110"
        align="center"
        prop="absenceWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.absenceWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="迟到扣款"
        min-width="110"
        align="center"
        prop="lateWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.lateWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="奖励工资"
        min-width="110"
        align="center"
        prop="bonusWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.bonusWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="其他工资"
        min-width="110"
        align="center"
        prop="etcWage"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.etcWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="工资总和"
        min-width="150"
        align="center"
        prop="totalWage"
        sortable
      >
        <template slot-scope="{ row }">
          <el-tag>{{ row.totalWage }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        width="150"
        class-name="small-padding fixed-width"
        fixed="right"
        v-if="upperRightVisible"
      >
        <template slot-scope="{ row }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
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

        <el-form-item label="工资月份" prop="wageDate">
          <el-input
            :disabled="dialogStatus === 'update'"
            v-model="temp.wageDate"
          >
          </el-input>
        </el-form-item>

        <el-form-item label="加班工资" prop="overtimeWage">
          <el-input v-model="temp.overtimeWage" />
        </el-form-item>

        <el-form-item label="出差补贴" prop="businessTripWage">
          <el-input v-model="temp.businessTripWage" />
        </el-form-item>

        <el-form-item label="旷工扣款" prop="absenceWage">
          <el-input v-model="temp.absenceWage" />
        </el-form-item>

        <el-form-item label="迟到扣款" prop="lateWage">
          <el-input v-model="temp.lateWage" />
        </el-form-item>

        <el-form-item label="奖励工资" prop="bonusWage">
          <el-input v-model="temp.bonusWage" />
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
import { getEmpMonthlyWageList, updateEmpMonthlyWage } from "@/api/wage";
import waves from "@/directive/waves"; // waves directive
import { parseTime } from "@/utils";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { get, set } from "js-cookie";
import UploadExcelComponent from "@/components/UploadExcel/index.vue";
import { getOneEmpWage } from "@/api/normal";

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
      upperRightVisible: this.$store.state.user.right == 10,
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
        empID: 0,
        wageDate: "",
        baseWage: 0.0,
        dayWage: 0.0,
        overtimeWage: 0.0,
        seniorityWage: 0.0,
        businessTripWage: 0.0,
        absenceWage: 0.0,
        lateWage: 0.0,
        bonusWage: 0.0,
        etcWage: 0.0,
        totalWage: 0.0,
      },
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑员工信息",
        create: "增加员工信息",
      },
      rules: {
        empName: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
        empSex: [{ required: true, message: "性别不能为空", trigger: "blur" }],
        empBirth: [
          { required: true, message: "出生日期不能为空", trigger: "blur" },
        ],
        identityID: [
          { required: true, message: "身份证号不能为空", trigger: "blur" },
        ],
        empTele: [{ required: true, message: "电话不能为空", trigger: "blur" }],
        hireDate: [
          { required: true, message: "入职时间不能为空", trigger: "blur" },
        ],
        duty: [{ required: true, message: "职务不能为空", trigger: "blur" }],
        departID: [
          { required: true, message: "部门不能为空", trigger: "blur" },
        ],
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
      if (this.$store.state.user.right == 0) {
        getOneEmpWage(this.$store.state.user.name).then((response) => {
          this.list = response.data;
          this.allData = response.data;
          this.total = this.list.length;
          console.log(this.list);

          this.list = this.list.slice(
            (this.listQuery.page - 1) * this.listQuery.limit,
            this.listQuery.page * this.listQuery.limit
          );
          setTimeout(() => {
            this.listLoading = false;
          }, 1.5 * 1000);
        });
      } else {
        getEmpMonthlyWageList().then((response) => {
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
      }
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
            updateEmpMonthlyWage(tempData).then((response) => {
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
        const tHeader = [
          "员工ID",
          "工资月份",
          "基本工资",
          "日薪合计",
          "加班工资",
          "工龄工资",
          "出差补贴",
          "旷工扣款",
          "迟到扣款",
          "奖励工资",
          "其他工资",
          "工资总和",
        ];
        const filterVal = [
          "empID",
          "wageDate",
          "baseWage",
          "dayWage",
          "overtimeWage",
          "seniorityWage",
          "businessTripWage",
          "absenceWage",
          "lateWage",
          "bonusWag",
          "etcWage",
          "totalWage",
        ];

        const data = this.formatJson(filterVal);

        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: "员工工资表",
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
        emp["empName"] = data[keys[0]];
        emp["empSex"] = data[keys[1]];
        emp["empBirth"] = data[keys[2]];
        emp["duty"] = data[keys[3]];
        emp["departID"] = data[keys[4]];
        emp["identityID"] = data[keys[5]];
        emp["empTele"] = data[keys[6]];
        emp["hireDate"] = data[keys[7]];
        emp["remark"] = data[keys[8]];
        if (
          emp["empName"] === undefined ||
          emp["empSex"] === undefined ||
          emp["empBirth"] === undefined ||
          emp["duty"] === undefined ||
          emp["departID"] === undefined ||
          emp["identityID"] === undefined ||
          emp["empTele"] === undefined ||
          emp["hireDate"] === undefined
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
      a.href = "/static/员工信息表模板.xlsx"; // 给a标签的href属性值加上地址，注意，这里是绝对路径，不用加 点.
      a.download = "员工信息表模板.xlsx"; //设置下载文件文件名，这里加上.xlsx指定文件类型，pdf文件就指定.fpd即可
      a.style.display = "none"; // 障眼法藏起来a标签
      document.body.appendChild(a); // 将a标签追加到文档对象中
      a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
      a.remove(); // 一次性的，用完就删除a标签
    },
  },
};
</script>
  