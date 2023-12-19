create trigger delete_emp_users 
on emp for update as
begin
    if update(LeaveDate)
    begin
        declare @empid nvarchar(10)
        declare @leavedate date
        declare @Duty nvarchar(20)
        declare @DepartID int
        declare c11 cursor for select empid,leavedate,Duty,DepartID from inserted
        open c11
        fetch next from c11 into @empid,@leavedate,@Duty,@DepartID
        while @@fetch_status=0
        begin
            --如果是主管，将部门表中的主管ID置空
            select @Duty=Duty from emp where empid=@empid
            if @Duty='主管'
            begin
                update Depart set DepartManagerID=NULL where DepartID=@DepartID
            end
            --删除用户表中的记录
            delete from [Users] where username=@empid
            --逻辑删除员工表中的记录
            update emp set LogicalDelete = 1 where empid=@empid
            --计算最后一次月薪
            insert into monthlywage(empid,WageDate) values(@empid,@leavedate)
            fetch next from c11 into @empid,@leavedate,@Duty,@DepartID
        end
        close c11
        deallocate c11
    end
end

update emp set leavedate=getdate() where empid=27