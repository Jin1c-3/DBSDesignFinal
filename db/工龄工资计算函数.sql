create function get_seniority_wage(@Empid int,@WageDate date,@DefaultWage money=50)
returns int
begin
    --指的是工龄，使用年份计时
    declare @seniority int
    select @seniority=datediff(year,HireDate,@WageDate) 
    from emp
    where empid=@Empid

    --工龄工资最多发放5年
    if(@seniority<5)
        return (@seniority)*@DefaultWage
    return 5*@DefaultWage
end

select dbo.get_seniority_wage(3,getdate(),default)