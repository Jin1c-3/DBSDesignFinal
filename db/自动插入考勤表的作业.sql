DECLARE @EmpID INT
DECLARE @LateDay INT
DECLARE @OvertimeHour FLOAT
declare emp_cur CURSOR FOR SELECT EmpID
FROM Emp
where LogicalDelete=0
OPEN emp_cur
FETCH NEXT FROM emp_cur INTO @EmpID
WHILE @@FETCH_STATUS = 0
BEGIN
    SET @LateDay=CAST(floor(rand()*3) as int)
    SET @OverTimeHour=RAND()
    if @OvertimeHour>0.5
    BEGIN
        SET @OvertimeHour=@OvertimeHour*10
    END
    ELSE
    BEGIN
        SET @OvertimeHour=0
    END
    INSERT INTO DailyCheckStat
        (EmpID, CheckDate, OvertimeHour, WorkDay, BusinessTripDay, AbsenceDay, LateDay)
    VALUES
        (@EmpID, GETDATE()-1, @OvertimeHour, 1, 0, 0, @LateDay)
    FETCH NEXT FROM emp_cur INTO @EmpID
END