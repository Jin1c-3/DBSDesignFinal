USE [WageManagement]
GO
/****** Object:  UserDefinedFunction [dbo].[get_BussinessTripWage]    Script Date: 2023/4/17 16:45:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER function [dbo].[get_BussinessTripWage](@Empid int,@WageDate date,@InputWage money)
returns money
as
begin
    --存储每日标准工资和标准出差工资补贴
    declare @DayWage money,@BusinessTripStd money
    --存储出差工时，允许小数点
    declare @BusinessTripDay float

    select @BusinessTripDay=sum(BusinessTripDay) from DailyCheckStat 
    where Empid=@Empid and month(CheckDate)=month(@WageDate) and year(CheckDate)=year(@WageDate)

    --·如果没有出差工时，则返回0
    if @BusinessTripDay is NULL
	begin
		return 0
	end

    --如果没有指定默认小时出差工资，则从工资配置表中读取
    if(@InputWage is null)
    begin
        select @BusinessTripDay=BusinessTripStd 
        from WageConfig where Duty=(select Duty from emp where empid=@Empid)
        
        return @BusinessTripDay*@BusinessTripStd
    end

    return @BusinessTripDay*@InputWage
end
