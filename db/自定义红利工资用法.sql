--需要输入日期、员工编号、自定义红利金额
update MonthlyWage set BonusWage=dbo.get_BonusWage(25,GETDATE(),5000,1) 
where empid=25 and year(WageDate)=year(getdate()) and MONTH(wagedate)=MONTH(getdate())