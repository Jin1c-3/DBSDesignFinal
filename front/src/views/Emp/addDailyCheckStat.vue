<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="员工ID">
        <el-input v-model="form.empID" />
      </el-form-item>

      <el-form-item label="考勤日期">
        <div class="block">
          <el-date-picker
            v-model="form.checkDate"
            align="right"
            type="date"
            placeholder="选择日期"
            :picker-options="pickerOptions"
          >
          </el-date-picker>
        </div>
      </el-form-item>

      <el-form-item label="加班小时数">
        <el-input v-model="form.overtimeHour" />
      </el-form-item>

      <el-form-item label="是否上班">
        <el-switch
          v-model="form.WorkDay"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="是"
          inactive-value="否"
        >
        </el-switch>
      </el-form-item>

      <el-form-item label="是否出差">
        <el-switch
          v-model="form.BusinessTripDay"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="是"
          inactive-value="否"
        >
        </el-switch>
      </el-form-item>

      <el-form-item label="是否旷工">
        <el-switch
          v-model="form.AbsenceDay"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="是"
          inactive-value="否"
        >
        </el-switch>
      </el-form-item>

      <el-form-item label="迟到次数">
        <el-input v-model="form.lateDay" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">提交记录</el-button>
        <el-button @click="onCancel">取消上传</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
  
<script>
import { addDailyCheckStat } from "@/api/emp";

export default {
  data() {
    return {
      form: {
        empID: "",
        checkDate: "",
        overtimeHour: 0.0,
        WorkDay: "是",
        BusinessTripDay: "否",
        AbsenceDay: "否",
        lateDay: 0,
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [
          {
            text: "今天",
            onClick(picker) {
              picker.$emit("pick", new Date());
            },
          },
          {
            text: "昨天",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
    };
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          console.log("addDailyCheckStat的表单数据如下");
          console.log(this.form);
          // 将信息传送到后端，存储到数据库
          addDailyCheckStat(this.form).then((res) => {
            this.$message({
              message: "添加成功",
              type: "success",
            });
            // this.$router.push({ path: "/query/record" }); //跳转到打卡查看表格
            // this.$refs.form.resetFields();
          });
        } else {
          console.log("error submit!!");
          this.$message({
            message: "添加失败！",
            type: "error",
          });
          return false;
        }
      });
    },

    onCancel() {
      this.$message({
        message: "cancel!",
        type: "warning",
      });
    },
  },
};
</script>
  
<style scoped>
.line {
  text-align: center;
}
</style>
  
  