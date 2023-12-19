create function get_BussinessTripWage(@Empid int,@WageDate date,@InputWage money)
returns money
as
begin
    --存储每日标准工资和标准出差工资补贴
    declare @DayWage money,@BussinessTripStd money
    --存储出差工时，允许小数点
    declare @BussinessTripDay float

    select @BussinessTripDay=sum(BussinessTripDay) from DailyCheckStat 
    where Empid=@Empid and month(CheckDate)=month(@WageDate) and year(CheckDate)=year(@WageDate)

    --·如果没有出差工时，则返回0
    if @BussinessTripDay is NULL
	begin
		return 0
	end

    --如果没有指定默认小时出差工资，则从工资配置表中读取
    if(@InputWage is null)
    begin
        select @BussinessTripStd=BussinessTripStd 
        from WageConfig where Duty=(select Duty from emp where empid=@Empid)
        
        return @BussinessTripDay*@BussinessTripStd
    end

    return @BussinessTripDay*@InputWage
end

select dbo.get_BussinessTripWage(3,getdate(),default)
select dbo.get_BussinessTripWage(3,getdate(),300)