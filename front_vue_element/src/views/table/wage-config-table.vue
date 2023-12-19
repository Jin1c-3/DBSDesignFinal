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
        class="filter-item"
        v-waves
        style="margin-left: 10px"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        增加
      </el-button>
      <el-button
        style="margin-left: 10px"
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-upload"
        @click="handleExcelUpload"
      >
        上传
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
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
      @sort-change="sortChange"
    >
      <el-table-column
        label="职务"
        prop="duty"
        sortable="custom"
        align="center"
        min-width="110"
      >
        <template slot-scope="{ row }">
          <span>{{ row.duty }}</span>
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
        sortable
        label="日薪"
        min-width="80"
        align="center"
        prop="dayWage"
      >
        <template slot-scope="{ row }">
          <span>{{ row.dayWage }}</span>
        </template>
      </el-table-column>

      <el-table-column
        sortable
        label="加班补贴比率"
        min-width="130"
        align="center"
        prop="overtimeRatio"
      >
        <template slot-scope="{ row }">
          <span>{{ row.overtimeRatio }}</span>
        </template>
      </el-table-column>

      <el-table-column
        sortable
        label="标准出差补贴"
        min-width="130"
        align="center"
        prop="businessTripStd"
      >
        <template slot-scope="{ row }">
          <span>{{ row.businessTripStd }}</span>
        </template>
      </el-table-column>

      <el-table-column
        sortable
        label="标准旷工扣款"
        min-width="130"
        align="center"
        prop="absenceStd"
      >
        <template slot-scope="{ row }">
          <span>{{ row.absenceStd }}</span>
        </template>
      </el-table-column>

      <el-table-column
        sortable
        label="标准迟到扣款"
        min-width="130"
        align="center"
        prop="lateStd"
      >
        <template slot-scope="{ row }">
          <span>{{ row.lateStd }}</span>
        </template>
      </el-table-column>

      <el-table-column
        sortable
        label="标准奖励"
        min-width="110"
        align="center"
        prop="bonus"
      >
        <template slot-scope="{ row }">
          <span>{{ row.bonus }}</span>
        </template>
      </el-table-column>

      <el-table-column
        sortable
        label="其他"
        min-width="100"
        align="center"
        prop="etcStd"
      >
        <template slot-scope="{ row }">
          <span>{{ row.etcStd }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        width="180"
        class-name="small-padding fixed-width"
        fixed="right"
      >
        <template slot-scope="{ row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>

          <el-button
            v-if="row.status != 'deleted'"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
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
        <el-form-item label="职务" prop="duty">
          <el-input :disabled="dialogStatus === 'update'" v-model="temp.duty" />
        </el-form-item>

        <el-form-item label="基本工资" prop="baseWage">
          <el-input v-model="temp.baseWage" />
        </el-form-item>

        <el-form-item label="日薪" prop="dayWage">
          <el-input v-model="temp.dayWage" />
        </el-form-item>

        <el-form-item label="加班补贴比率" prop="overtimeRatio">
          <el-input v-model="temp.overtimeRatio" />
        </el-form-item>

        <el-form-item label="标准出差补贴" prop="businessTripStd">
          <el-input v-model="temp.businessTripStd" />
        </el-form-item>

        <el-form-item label="标准旷工扣款" prop="absenceStd">
          <el-input v-model="temp.absenceStd" />
        </el-form-item>

        <el-form-item label="标准迟到扣款" prop="lateStd">
          <el-input v-model="temp.lateStd" />
        </el-form-item>

        <el-form-item label="标准奖励" prop="bonus">
          <el-input v-model="temp.bonus" />
        </el-form-item>

        <el-form-item label="其他" prop="etcStd">
          <el-input v-model="temp.etcStd" />
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
  getWageConfigList,
  updateWageConfig,
  addWageConfigList,
  deleteWageConfig,
  addWageConfig,
} from "@/api/wage";
import waves from "@/directive/waves"; // waves directive
import { parseTime } from "@/utils";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { get, set } from "js-cookie";
import UploadExcelComponent from "@/components/UploadExcel/index.vue";

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
      list: [],
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
        title: "",
        type: "",
        status: "published",
        duty: "",
        baseWage: 0,
        dayWage: 0,
        overtimeRatio: 0,
        businessTripStd: 0,
        absenceStd: 0,
        lateStd: 0,
        bonus: 0,
        etcStd: 0,
      },
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑工资配置信息",
        create: "增加工资配置信息",
      },
      rules: {
        duty: [{ required: true, message: "不能为空", trigger: "blur" }],
        baseWage: [{ required: true, message: "不能为空", trigger: "blur" }],
        dayWage: [{ required: true, message: "不能为空", trigger: "blur" }],
        overtimeRatio: [
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        businessTripStd: [
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        absenceStd: [{ required: true, message: "不能为空", trigger: "blur" }],
        lateStd: [{ required: true, message: "不能为空", trigger: "blur" }],
        bonus: [{ required: true, message: "不能为空", trigger: "blur" }],
        etcStd: [{ required: true, message: "不能为空", trigger: "blur" }],
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
      getWageConfigList().then((response) => {
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
      if (prop === "duty") {
        console.log("sort by duty");
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
        this.listQuery.sort = "+duty";
        this.list.sort((a, b) => a.duty.localeCompare(b.duty));
      } else {
        // this.listQuery.sort = "-id";
        this.listQuery.sort = "-duty";
        this.list.sort((b, a) => a.duty.localeCompare(b.duty));
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
          addWageConfig(this.temp).then(() => {
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
          console.log(tempData);
          updateWageConfig(tempData).then((response) => {
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
      });
    },
    handleDelete(row) {
      deleteWageConfig(row.duty).then((response) => {
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
          "职务",
          "基本工资",
          "日薪",
          "加班补贴比率",
          "标准出差补贴",
          "标准旷工扣款",
          "标准迟到扣款",
          "标准奖励",
          "其他",
        ];
        const filterVal = [
          "duty",
          "baseWage",
          "dayWage",
          "overtimeRatio",
          "businessTripStd",
          "absenceStd",
          "lateStd",
          "bonus",
          "hietcStdreDate",
        ];

        const data = this.formatJson(filterVal);

        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: "员工信息表",
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
      let wageConfigList = [];
      for (let data of this.tableData) {
        let wageConfig = {};
        let keys = Object.keys(data);
        wageConfig["duty"] = data[keys[0]];
        wageConfig["baseWage"] = data[keys[1]];
        wageConfig["dayWage"] = data[keys[2]];
        wageConfig["overtimeRatio"] = data[keys[3]];
        wageConfig["businessTripStd"] = data[keys[4]];
        wageConfig["absenceStd"] = data[keys[5]];
        wageConfig["lateStd"] = data[keys[6]];
        wageConfig["bonus"] = data[keys[7]];
        wageConfig["etcStd"] = data[keys[8]];
        if (
          wageConfig["duty"] == null ||
          wageConfig["baseWage"] == null ||
          wageConfig["dayWage"] == null ||
          wageConfig["overtimeRatio"] == null ||
          wageConfig["businessTripStd"] == null ||
          wageConfig["absenceStd"] == null ||
          wageConfig["lateStd"] == null ||
          wageConfig["bonus"] == null
        ) {
          this.$notify({
            title: "Error",
            message: "请检查数据是否完整",
            type: "error",
            duration: 2000,
          });
          return;
        }
        wageConfigList.push(wageConfig);
      }
      addWageConfigList(wageConfigList).then((response) => {
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
      a.href = "/static/工资配置信息表模板.xlsx"; // 给a标签的href属性值加上地址，注意，这里是绝对路径，不用加 点.
      a.download = "工资配置信息表模板.xlsx"; //设置下载文件文件名，这里加上.xlsx指定文件类型，pdf文件就指定.fpd即可
      a.style.display = "none"; // 障眼法藏起来a标签
      document.body.appendChild(a); // 将a标签追加到文档对象中
      a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
      a.remove(); // 一次性的，用完就删除a标签
    },
  },
};
</script>
  