declare c1 cursor for
select empid,CheckDate from DailyCheckStat
open c1
declare @empid int
declare @CheckDate date
fetch next from c1 into @empid,@CheckDate
while @@fetch_status=0
begin
    if @CheckDate<(select Hiredate from emp where empid=@empid)
    begin
        delete from DailyCheckStat where current of c1
    end
    fetch next from c1 into @empid,@CheckDate
end