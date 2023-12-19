create function get_EtcWage(@Empid int,@WageDate date,@InputWage money,@Flag bit=0)
returns money
as
begin
	--Flag=1表示强制发放其他工资
	if(@Flag=1)
	begin
		--存储标准其他工资
		declare @EtcWage money

		--如果没有指定其他工资，则从工资配置表中读取
		if(@InputWage is null)
		begin
			select @EtcWage=EtcStd
			from WageConfig where Duty=(select Duty from emp where empid=@Empid)
		end
		else
		begin
			select @EtcWage=@InputWage
		end
		return @EtcWage
	end
	return 0
end

select dbo.get_EtcWage(3,GETDATE(),default,1)
select dbo.get_EtcWage(3,GETDATE(),default,0)