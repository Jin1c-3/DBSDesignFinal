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
        v-if="this.$store.state.user.right == 1"
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
        v-if="this.$store.state.user.right == 1"
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
        min-width="110"
        align="center"
        prop="empName"
        sortable="custom"
      >
        <template slot-scope="{ row }">
          <span>{{ row.empName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="员工性别" width="80" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.empSex }}</span>
        </template>
      </el-table-column>

      <el-table-column label="员工生日" min-width="100" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.empBirth | parseTime("{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>

      <el-table-column label="职务" width="100" align="center">
        <template slot-scope="{ row }">
          <el-tag>{{ row.duty }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="所属部门ID"
        min-width="120"
        align="center"
        prop="departID"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.departID }}</span>
        </template>
      </el-table-column>

      <el-table-column label="身份证号" min-width="180" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.identityID }}</span>
        </template>
      </el-table-column>

      <el-table-column label="电话号码" min-width="120" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.empTele }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="入职时间"
        min-width="120"
        align="center"
        prop="hireDate"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.hireDate | parseTime("{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>

      <el-table-column label="备注" align="center" min-width="200px">
        <template slot-scope="{ row }">
          <div style="max-height: 80px; overflow-y: auto">
            <span>{{ row.empRemark }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="员工图片" align="center" min-width="120px">
        <template slot-scope="{ row }">
          <el-button @click="handleView(row)" v-if="row.empPic" type="success"
            >查看图片</el-button
          >
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
        <el-form-item
          label="员工ID"
          prop="empID"
          v-if="dialogStatus === 'update'"
        >
          <el-input
            :disabled="dialogStatus === 'update'"
            v-model="temp.empID"
          />
        </el-form-item>

        <el-form-item
          label="员工姓名"
          v-if="dialogStatus === 'create' && this.$store.state.user.right == 1"
          prop="empName"
        >
          <el-input v-model="temp.empName" />
        </el-form-item>

        <el-form-item
          label="性别"
          v-if="dialogStatus === 'create' && this.$store.state.user.right == 1"
          prop="empSex"
        >
          <el-select v-model="temp.empSex" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          label="生日"
          prop="empBirth"
          v-if="dialogStatus === 'create' && this.$store.state.user.right == 1"
        >
          <el-date-picker
            v-model="temp.empBirth"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item
          label="身份证号"
          v-if="dialogStatus === 'create' && this.$store.state.user.right == 1"
          prop="identityID"
        >
          <el-input v-model="temp.identityID" />
        </el-form-item>

        <el-form-item
          label="职务"
          prop="duty"
          v-if="this.$store.state.user.right == 1"
        >
          <el-input v-model="temp.duty" />
        </el-form-item>

        <el-form-item
          label="部门ID"
          prop="departID"
          v-if="this.$store.state.user.right == 1"
        >
          <el-input v-model="temp.departID" />
        </el-form-item>

        <el-form-item label="电话" prop="empTele">
          <el-input v-model="temp.empTele" />
        </el-form-item>

        <el-form-item
          label="入职时间"
          prop="hireDate"
          v-if="dialogStatus === 'create' && this.$store.state.user.right == 1"
        >
          <el-date-picker
            v-model="temp.hireDate"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item
          prop="empPic"
          label="员工图片"
          v-if="dialogStatus === 'create'"
        >
          <!-- 上传图片组件 action为接收上传的后端地址 -->
          <el-upload
            v-if="isShowUpload"
            ref="upload"
            class="avatar-uploader"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :headers="headers"
            :limit="1"
            :on-exceed="handleExceed"
            :auto-upload="false"
            :on-change="onChange"
            accept=".jpg,.png,.jpeg"
            name="file"
            v-model="temp.empPic"
          >
            <i class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>

        <el-row v-if="isShowImgUpload && dialogStatus === 'create'">
          <el-col :span="6" style="color: white">1</el-col>
          <el-col :span="10">
            <div style="position: relative; background-color: #f0f3fa">
              <el-image
                :src="localUrl"
                :preview-src-list="[localUrl]"
                style="width: 100%; height: 100%"
                fit="scale-down"
              ></el-image>
            </div>
            <div style="padding: 10% 40%">
              <el-button
                type="primary"
                :loading="false"
                size="small"
                @click="reLoad"
                >重新上传</el-button
              >
            </div>
          </el-col>
          <el-col :span="4" style="color: white">1</el-col>
        </el-row>

        <el-form-item
          label="备注"
          prop="empRemark"
          v-if="this.$store.state.user.right == 1"
        >
          <el-input
            :autosize="{ minRows: 2, maxRows: 4 }"
            type="textarea"
            placeholder="请输入备注信息"
            v-model="temp.empRemark"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="
            dialogFormVisible = false;
            localUrl = '';
            isShowImgUpload = false;
            isShowUpload = true;
          "
        >
          取消
        </el-button>
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
  getEmpPic,
} from "@/api/emp";
import waves from "@/directive/waves"; // waves directive
import { parseTime } from "@/utils";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { get, set } from "js-cookie";
import UploadExcelComponent from "@/components/UploadExcel/index.vue";
import { getOneEmp, updateOneEmp } from "@/api/normal";

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
      isShowImgUpload: false,
      isShowUpload: true,

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
        empName: "",
        remark: "",
        title: "",
        type: "",
        status: "published",
        empID: "",
        duty: "",
        departID: "",
        empRemark: "",
        empTele: "",
        hireDate: "",
        empName: "",
        empSex: "",
        empBirth: "",
        identityID: "",
        file: "",
        empPic: [],
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
        getOneEmp(this.$store.state.user.name).then((response) => {
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
        getEmpList().then((response) => {
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
          console.log(this.temp);
          let formDate = new FormData();
          formDate.append("empName", this.temp.empName);
          formDate.append("empSex", this.temp.empSex);
          formDate.append("empBirth", this.temp.empBirth);
          formDate.append("identityID", this.temp.identityID);
          formDate.append("empTele", this.temp.empTele);
          formDate.append("hireDate", this.temp.hireDate);
          formDate.append("duty", this.temp.duty);
          formDate.append("departID", this.temp.departID);
          formDate.append("file", this.temp.file);
          addEmp(formDate).then(() => {
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
        const tHeader = [
          "员工ID",
          "员工姓名",
          "员工性别",
          "员工生日",
          "职务",
          "所属部门ID",
          "身份证号",
          "电话号码",
          "入职时间",
          "备注",
        ];
        const filterVal = [
          "empID",
          "empName",
          "empSex",
          "empBirth",
          "duty",
          "departID",
          "identityID",
          "empTele",
          "hireDate",
          "remark",
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
    //可用于预览图片
    onChange(event) {
      const isPic =
        ["image/jpeg", "image/png", "image/jpg"].indexOf(event.raw.type) != -1;
      const isLt = event.raw.size / 1024 / 1024 < 5;
      if (!isPic) {
        this.$message({
          type: "error",
          message: "上传的图片只能是 png 或 jpg 格式!",
          duration: 1000 * 3,
        });
      }
      if (!isLt) {
        this.$message({
          type: "error",
          message: "上传图片大小不能超过 5MB!",
          duration: 1000 * 2,
        });
      }
      if (!isPic || !isLt) {
        this.form.file = ""; //清除表单文件记录
        this.$refs.upload.clearFiles(); //不符合的文件类型，清除已经上传的文件
        return;
      }

      //event.raw为图片实体，可通过consolse打印查看详情
      // 获取上传图片的本地URL，用于上传前的本地预览
      var URL = null;
      if (window.createObjectURL != undefined) {
        // basic
        URL = window.createObjectURL(event.raw);
      } else if (window.URL != undefined) {
        // mozilla(firefox)
        URL = window.URL.createObjectURL(event.raw);
      } else if (window.webkitURL != undefined) {
        // webkit or chrome
        URL = window.webkitURL.createObjectURL(event.raw);
      }
      // 转换后的地址为 blob:http://xxx/7bf54338-74bb-47b9-9a7f-7a7093c716b5
      this.localUrl = URL;
      this.isShowImgUpload = true; //呈现本地预览组件
      this.isShowUpload = false; //隐藏上传组件
      this.temp.file = event.raw;
      console.log("the file of form is-->");
      console.log(this.temp);
    },
    //重新选择图片
    reLoad() {
      this.isShowImgUpload = false; //呈现本地预览组件
      this.isShowUpload = true; //隐藏上传组件
      this.temp.file = ""; //清除表单中的文件记录
      this.localUrl = "";
      this.$refs.upload.clearFiles(); //不符合的文件类型，清除已经上传的文件
    },

    handleExceed() {
      this.$message({
        type: "error",
        message: "只能上传1张图片！",
        duration: 1000 * 3,
      });
    },

    handleView(row) {
      getEmpPic(row.empID).then((res) => {
        let URL = null;
        let binaryData = [];
        binaryData.push(res);
        let pic = new Blob(binaryData, {
          type: "image/jpeg",
        });
        if (window.createObjectURL != undefined) {
          // basic
          URL = window.createObjectURL(pic);
        } else if (window.URL != undefined) {
          // mozilla(firefox)
          URL = window.URL.createObjectURL(pic);
        } else if (window.webkitURL != undefined) {
          // webkit or chrome
          URL = window.webkitURL.createObjectURL(pic);
        }
        var link = document.createElement("a");
        link.href = URL;
        link.target = "_blank";
        link.click();
      });
    },
  },
};
</script>
