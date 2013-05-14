-- Create GetSequenceNextValue function

CREATE FUNCTION GetSequenceNextValue(@SequenceNameIn VARCHAR(255))
  RETURNS BIGINT
  AS
  BEGIN
	RETURN (SELECT SEQUENCE_NEXT_VALUE
			FROM TB_SEQUENCES 
			WHERE SEQUENCE_NAME = @SequenceNameIn)
  END;
GO