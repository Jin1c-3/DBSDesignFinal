create function get_BonusWage(@Empid int,@WageDate date,@InputWage money,@Flag bit=0)
returns money
as
begin
    --存储标准红利奖金
    declare @Bonus money
	declare @LateDay int

    select @LateDay=sum(LateDay) from DailyCheckStat 
    where Empid=@Empid and month(CheckDate)=month(@WageDate) and year(CheckDate)=year(@WageDate)

    --如果没有指定红利奖金，则从工资配置表中读取
    if(@InputWage is null)
    begin
        select @Bonus=Bonus 
        from WageConfig where Duty=(select Duty from emp where empid=@Empid)
    end
    else
    begin
        select @Bonus=@InputWage
    end

    --如果全勤或者Flag=1，返回红利奖金，否则返回0
    --Flag=1表示强制发放红利奖金
    if(@LateDay=0 or @Flag=1)
        return @Bonus
    return 0
end

select dbo.get_BonusWage(7,GETDATE(),default,1)