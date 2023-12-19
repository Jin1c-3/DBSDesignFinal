create function get_LateWage(@Empid int,@WageDate date,@InputWage money)
returns money
as
begin
    --存储每日标准工资和标准迟到惩罚
    declare @LateStd money
    --存储出差工时，允许小数点
    declare @LateDay int

    select @LateDay=sum(LateDay) from DailyCheckStat 
    where Empid=@Empid and month(CheckDate)=month(@WageDate) and year(CheckDate)=year(@WageDate)

    --如果没有迟到天数，则返回0
    if @LateDay is NULL
	begin
		return 0
	end

    --如果没有指定迟到惩罚工资，则从工资配置表中读取
    if(@InputWage is null)
    begin
        select @LateStd=LateStd 
        from WageConfig where Duty=(select Duty from emp where empid=@Empid)
        
        return -@LateDay*@LateStd
    end

    return -@LateDay*@InputWage
end

select dbo.get_LateWage(1,'2023-02-01',default)
select dbo.get_LateWage(1,'2023-02-01',1000)
select dbo.get_LateWage(1,GETDATE(),1000)