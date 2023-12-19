create trigger alter_emp_depart
on emp for update as
begin
    declare @EmpID int,@Duty nvarchar(20),@DepartID int
    declare alter_emp_depart_c1 cursor for select EmpID,Duty,DepartID from inserted
    open alter_emp_depart_c1
    fetch next from alter_emp_depart_c1 into @EmpID,@Duty,@DepartID
    while @@fetch_status=0
    begin
        if @Duty='主管'
        begin
            declare @DepartManagerID int
            select @DepartManagerID=DepartManagerID from Depart where DepartID=@DepartID
            if @DepartManagerID='' or @DepartManagerID is null
            begin
                print '该部门没有主管，现在设置该员工为主管'
                update Depart set DepartManagerID=@EmpID where DepartID=@DepartID
            end
            else
            begin
                print '每个部门只允许有一个主管，该部门已经有主管了'
                rollback transaction
            end
        end
        fetch next from alter_emp_depart_c1 into @EmpID,@Duty,@DepartID
    end
    close alter_emp_depart_c1
    deallocate alter_emp_depart_c1

    declare alter_emp_depart_c2 cursor for select EmpID,Duty,DepartID from deleted
    open alter_emp_depart_c2
    fetch next from alter_emp_depart_c2 into @EmpID,@Duty,@DepartID
    while @@fetch_status=0
    begin
        if @Duty='主管'
        begin
            update Depart set DepartManagerID=NULL where DepartID=@DepartID
        end
        fetch next from alter_emp_depart_c2 into @EmpID,@Duty,@DepartID
    end
	close alter_emp_depart_c2
	deallocate alter_emp_depart_c2
end