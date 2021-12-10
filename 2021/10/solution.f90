! Try it here: https://www.tutorialspoint.com/compile_fortran_online.php
! Make sure to append "EOF" to the STDIN input

MODULE BracketStack
    IMPLICIT NONE

    INTEGER :: index = 1
    CHARACTER(128) :: items

    TYPE :: LineScore
        INTEGER(KIND=8) :: corrupted
        INTEGER(KIND=8) :: incomplete
    END TYPE

CONTAINS
    
    SUBROUTINE push(item)
        IMPLICIT NONE
        CHARACTER, INTENT(in) :: item
        items(index:index) = item
        index = index + 1
    END SUBROUTINE
    
    FUNCTION pop() RESULT(item)
        IMPLICIT NONE
        CHARACTER :: item
        index = index - 1
        item = items(index:index)
    END FUNCTION
    
    SUBROUTINE reset()
        IMPLICIT NONE
        index = 1
    END SUBROUTINE

    FUNCTION getLineScore(line) RESULT(score)
        IMPLICIT NONE
        CHARACTER(128), INTENT(in) :: line
        TYPE(LineScore) :: score
        INTEGER :: i
        CHARACTER :: character
        CALL reset()
        DO i = 1, 128
            character = line(i:i)
            SELECT CASE (character)
                CASE ("(")
                    CALL push(character)
                CASE ("[")
                    CALL push(character)
                CASE ("{")
                    CALL push(character)
                CASE ("<")
                    CALL push(character)
                CASE (")")
                    IF (pop() /= "(") THEN
                        score%corrupted = 3
                        score%incomplete = 0
                        EXIT
                    END IF
                CASE ("]")
                    IF (pop() /= "[") THEN
                        score%corrupted = 57
                        score%incomplete = 0
                        EXIT
                    END IF
                CASE ("}")
                    IF (pop() /= "{") THEN
                        score%corrupted = 1197
                        score%incomplete = 0
                        EXIT
                    END IF
                CASE (">")
                    IF (pop() /= "<") THEN
                        score%corrupted = 25137
                        score%incomplete = 0
                        EXIT
                    END IF
                CASE DEFAULT
                    score%corrupted = 0
                    score%incomplete = getIncompleteScore()
                    EXIT
            END SELECT
        END DO
    END FUNCTION

    FUNCTION getIncompleteScore() RESULT(score)
        IMPLICIT NONE
        INTEGER(KIND=8) :: score
        score = 0
        DO WHILE (index > 1)
            score = score * 5
            SELECT CASE (pop())
                CASE ("(")
                    score = score + 1
                CASE ("[")
                    score = score + 2
                CASE ("{")
                    score = score + 3
                CASE ("<")
                    score = score + 4
            END SELECT
        END DO
    END FUNCTION
    
END MODULE


MODULE MedianList
    IMPLICIT NONE
    INTEGER :: index = 1
    INTEGER(KIND=8) :: scores(128)

CONTAINS

    SUBROUTINE addMedianItem(score)
        IMPLICIT NONE
        INTEGER(KIND=8), INTENT(in) :: score
        scores(index:index) = score
        index = index + 1
    END SUBROUTINE
    
    SUBROUTINE bubbleSort()
        IMPLICIT NONE
        INTEGER(KIND=8) :: temp
        INTEGER(KIND=8) :: i
        INTEGER(KIND=8) :: j
        DO i = INDEX-1, 1, -1
           DO j = 1, i
              IF (scores(j+1) < scores(j)) THEN
                 temp=scores(j+1)
                 scores(j+1)=scores(j)
                 scores(j)=temp
              ENDIF
           ENDDO
        ENDDO
    END SUBROUTINE
    
    FUNCTION getMedian() RESULT(median)
        IMPLICIT NONE
        INTEGER(KIND=8) :: median
        CALL bubbleSort()
        median = scores(index/2 + 1)
    END FUNCTION
    
END MODULE


PROGRAM Solution
    USE BracketStack
    USE MedianList
    IMPLICIT NONE
    CHARACTER(128) :: line
    TYPE(LineScore) :: score
    INTEGER(KIND=8) :: part1 = 0
    READ(*,*) line
    DO WHILE (line /= "EOF")
        score = getLineScore(line)
        part1 = part1 + score%corrupted
        IF (score%corrupted == 0) THEN
            CALL addMedianItem(score%incomplete)
        END IF
        READ(*,*) line
    END DO
    PRINT *, "Part1:"
    PRINT *, part1
    PRINT *, "Part2:"
    PRINT *, getMedian()
END PROGRAM