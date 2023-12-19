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
        v-if="this.$store.state.user.right == 100"
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
        v-if="this.$store.state.user.right == 100"
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
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%"
      @sort-change="sortChange"
    >
      <el-table-column
        label="用户名"
        prop="userName"
        sortable
        align="center"
        width="100"
      >
        <template slot-scope="{ row }">
          <span>{{ row.userName }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="密码"
        min-width="110"
        align="center"
        prop="userPassword"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.userPassword }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="用户权限"
        min-width="110"
        align="center"
        prop="userRight"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.userRight }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="用户权限等级"
        min-width="110"
        align="center"
        prop="userRightLevel"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.userRightLevel }}</span>
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
            v-if="row.status != 'deleted' && rightLevelEq100"
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
        label-width="45%"
        style="width: 80%; margin-left: 10%"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input
            :disabled="dialogStatus === 'update'"
            v-model="temp.userName"
          />
        </el-form-item>

        <el-form-item label="密码" prop="userPassword">
          <el-input v-model="temp.userPassword" />
        </el-form-item>

        <el-form-item
          label="用户权限"
          v-if="this.$store.state.user.right == 100"
          prop="userRight"
        >
          <el-input v-model="temp.userRight" />
        </el-form-item>

        <el-form-item
          label="用户权限等级"
          prop="userRightLevel"
          v-if="this.$store.state.user.right == 100"
        >
          <el-input v-model="temp.userRightLevel" />
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
} from "@/api/emp";
import waves from "@/directive/waves"; // waves directive
import { parseTime } from "@/utils";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { get, set } from "js-cookie";
import UploadExcelComponent from "@/components/UploadExcel/index.vue";
import {
  getOneEmp,
  updateOneEmp,
  getOneUser,
  updateOneUser,
} from "@/api/normal";
import {
  getUserList,
  addUser,
  addUserList,
  deleteUser,
} from "@/api/super-user";

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
      rightLevelEq100: this.$store.state.user.right === 100,
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
        userName: "",
        userPassword: "",
        userRight: "",
        userRightLevel: "",
      },
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑员工信息",
        create: "增加员工信息",
      },
      rules: {
        userName: [{ required: true, message: "不能为空", trigger: "blur" }],
        userPassword: [
          { required: true, message: "不能为空", trigger: "blur" },
        ],
        userRightLevel: [
          { required: true, message: "不能为空", trigger: "blur" },
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
        getOneUser(this.$store.state.user.name).then((response) => {
          let tempList = [];
          tempList.push(response.data);
          this.list = tempList;
          this.allData = tempList;
          this.total = this.list.length;

          this.list = this.list.slice(
            (this.listQuery.page - 1) * this.listQuery.limit,
            this.listQuery.page * this.listQuery.limit
          );
          setTimeout(() => {
            this.listLoading = false;
          }, 1.5 * 1000);
        });
      } else {
        getUserList().then((response) => {
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
          addUser(this.temp).then(() => {
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
            updateOneUser(tempData).then((response) => {
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
      deleteUser(row.userName).then((response) => {
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
        const tHeader = ["用户名", "密码", "用户权限", "用户权限等级"];
        const filterVal = [
          "userName",
          "userPassword",
          "userRight",
          "userRightLevel",
        ];

        const data = this.formatJson(filterVal);

        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: "用户信息表",
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
      let userList = [];
      for (let data of this.tableData) {
        let user = {};
        let keys = Object.keys(data);
        user["userName"] = data[keys[0]];
        user["userPassword"] = data[keys[1]];
        user["UserRight"] = data[keys[2]];
        user["userRightLevel"] = data[keys[3]];
        if (
          user["userName"] === undefined ||
          user["userPassword"] === undefined ||
          user["UserRight"] === undefined ||
          user["userRightLevel"] === undefined
        ) {
          this.$notify({
            title: "Error",
            message: "请检查数据是否完整",
            type: "error",
            duration: 2000,
          });
          return;
        }
        userList.push(user);
      }
      addUserList(userList).then((response) => {
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
      a.href = "/static/用户信息表模板.xlsx"; // 给a标签的href属性值加上地址，注意，这里是绝对路径，不用加 点.
      a.download = "用户信息表模板.xlsx"; //设置下载文件文件名，这里加上.xlsx指定文件类型，pdf文件就指定.fpd即可
      a.style.display = "none"; // 障眼法藏起来a标签
      document.body.appendChild(a); // 将a标签追加到文档对象中
      a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
      a.remove(); // 一次性的，用完就删除a标签
    },
  },
};
</script>
  