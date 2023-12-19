create proc refreshAllMonthlyWage(@Date date)
as
begin
    declare @empid int
    declare refreshAllMonthlyWage_c1 cursor for
    select empid from emp where LogicalDelete=0
    open refreshAllMonthlyWage_c1
    fetch next from refreshAllMonthlyWage_c1 into @empid
    while @@fetch_status=0
    begin
        --如果传入日期，则只刷新该日期的月薪
        if @Date!=''
        begin
            insert into monthlywage(empid,WageDate) values(@empid,@Date)
        end
        --如果不传入日期，则刷新所有月薪
        else
        begin
            declare @WageDate date
            declare refreshAllMonthlyWage_c2 cursor for
            select distinct concat(trim(str(year(CheckDate))),'-',trim(str(month(CheckDate))),'-01') from DailyCheckStat
            where empid=@empid
            open refreshAllMonthlyWage_c2
            fetch next from refreshAllMonthlyWage_c2 into @WageDate
            while @@fetch_status=0
            begin
                insert into monthlywage(empid,WageDate) values(@empid,@WageDate)
                fetch next from refreshAllMonthlyWage_c2 into @WageDate
            end
            close refreshAllMonthlyWage_c2
            deallocate refreshAllMonthlyWage_c2
        end
        fetch next from refreshAllMonthlyWage_c1 into @empid
    end
    close refreshAllMonthlyWage_c1
    deallocate refreshAllMonthlyWage_c1
end

declare @NowDate date =getdate()
exec refreshAllMonthlyWage @NowDate