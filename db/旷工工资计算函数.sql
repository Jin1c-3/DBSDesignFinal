create function get_AbsenceWage(@Empid int,@WageDate date,@InputWage money)
returns money
as
begin
    --存储每日标准工资和标准出差工资补贴
    declare @DayWage money,@AbsenceStd money
    --存储出差工时，允许小数点
    declare @AbsenceDay float

    select @AbsenceDay=sum(AbsenceDay) from DailyCheckStat 
    where Empid=@Empid and month(CheckDate)=month(@WageDate) and year(CheckDate)=year(@WageDate)

	--若查询结果为NULL则返回0
	if @AbsenceDay is NULL
	begin
		return 0
	end

    --如果没有指定默认小时出差工资，则从工资配置表中读取
    if(@InputWage is null)
    begin
        select @AbsenceStd=AbsenceStd 
        from WageConfig where Duty=(select Duty from emp where empid=@Empid)
        
        return -@AbsenceDay*@AbsenceStd
    end

    return -@AbsenceDay*@InputWage
end

select dbo.get_AbsenceWage(3,'2023-02-01',default)
select dbo.get_AbsenceWage(3,GETDATE(),1000)