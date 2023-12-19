create trigger wage_intsert_auto_add_other_column
on MonthlyWage
instead of insert
as
begin
    --声明inserted游标的变量
    declare @Empid int,@WageDate date,@LogicalDelete int
    --声明inserted游标
    declare c1 scroll cursor for
    select EmpID,WageDate from inserted
    open c1
    fetch next from c1 into @Empid,@WageDate
    while @@fetch_status=0
    begin
        select @LogicalDelete=LogicalDelete from emp where empid=@Empid
        --如果员工表中的记录已经被逻辑删除，则不允许插入工资表
        if @LogicalDelete=0
        begin
            --声明原表游标的变量
            declare @DEmpid int,@DWageDate date
            --声明原表游标
            declare c2 scroll cursor for
            select EmpID,WageDate from MonthlyWage
            open c2
            fetch next from c2 into @DEmpid,@DWageDate
            while @@fetch_status=0
            begin
                --如果原表中已经有了相同ID、相同年份和月份的记录，则删除
                if(@Empid=@DEmpid and year(@WageDate)=year(@DWageDate) and month(@WageDate)=month(@DWageDate))
                begin 
                    delete from MonthlyWage where EmpID=@DEmpid and WageDate=@DWageDate
                end
                fetch next from c2 into @DEmpid,@DWageDate
            end
            close c2
            deallocate c2
            insert into MonthlyWage(
                EmpID,
                WageDate,
                BaseWage,
                DayWage,
                OvertimeWage,
                SeniorityWage,
                BusinessTripWage,
                AbsenceWage,
                LateWage,
                BonusWage,
                EtcWage
            )
            values(
                @Empid,
                @WageDate,
                dbo.get_BaseWage(@Empid),
                dbo.get_DayWage(@Empid,@WageDate,default),
                dbo.get_overtime_wage(@Empid,@WageDate,default),
                dbo.get_seniority_wage(@Empid,@WageDate,default),
                dbo.get_BusinessTripWage(@Empid,@WageDate,default),
                dbo.get_AbsenceWage(@Empid,@WageDate,default),
                dbo.get_LateWage(@Empid,@WageDate,default),
                dbo.get_BonusWage(@Empid,@WageDate,default,default),
                dbo.get_EtcWage(@Empid,@WageDate,default,default)
            )
            fetch next from c1 into @Empid,@WageDate
        end
        else
        begin
            print '该员工已经离职，不允许插入工资表'
        end
    end
	
	close c1
	deallocate c1
end