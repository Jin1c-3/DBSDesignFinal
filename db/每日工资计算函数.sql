create function get_DayWage(@Empid int,@WageDate date,@InputWage money)
returns money
as
begin
    --存储每日标准工资
    declare @DayWage money
    --存储上班日，允许小数点
    declare @WorkDay float

    select @WorkDay=sum(WorkDay) from DailyCheckStat 
    where Empid=@Empid and month(CheckDate)=month(@WageDate) and year(CheckDate)=year(@WageDate)

    --·如果没有上班日，则返回0
    if @WorkDay is NULL
    begin
        return 0
    end

    --如果没有指定默认小时出差工资，则从工资配置表中读取
    if(@InputWage is null)
    begin
        select @DayWage=DayWage 
        from WageConfig where Duty=(select Duty from emp where empid=@Empid)
        
        return @WorkDay*@DayWage
    end

    return @WorkDay*@InputWage
end
