create function get_BaseWage(@Empid int)
returns money
as
begin
    declare @Duty nvarchar(20),@BaseWage money
    select @Duty=Duty from emp where empid=@Empid
    select @BaseWage=BaseWage from WageConfig where Duty=@Duty
    return @BaseWage
end