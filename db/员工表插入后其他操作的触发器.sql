create trigger insert_emp_user 
on emp for insert as
begin
	declare @EmpID nvarchar(20),@identityID varchar(18),@Duty nvarchar(20),@DepartID int
	declare c1 cursor for select EmpID,identityID,Duty,DepartID from inserted
	open c1
	fetch next from c1 into @EmpID,@identityID,@Duty,@DepartID
	while @@fetch_status=0
	begin
		if @Duty='主管'
		begin
			declare @DepartManagerID int
			select @DepartManagerID=DepartManagerID from Depart where DepartID=@DepartID
			if @DepartManagerID is not null
			begin
				update Depart set DepartManagerID=@EmpID where DepartID=@DepartID
			end
			else
			begin
				print '每个部门只允许有一个主管，该部门已经有主管了'
				rollback transaction
			end
		end
		insert into [Users] (username,userpassword)
		values(@EmpID,upper(right(@identityID,6)))
		fetch next from c1 into @EmpID,@identityID,@Duty,@DepartID
	end
	close c1
	deallocate c1
end