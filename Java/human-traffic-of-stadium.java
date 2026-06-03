/* Write your T-SQL query statement below */
SELECT
    s1.id,
    s1.visit_date,
    s1.people
FROM
    Stadium s1,
    Stadium s2,
    Stadium s3
WHERE
    s1.people >= 100 AND s2.people >= 100 AND s3.people >= 100
    AND (
        (s1.id = s2.id - 1 AND s1.id = s3.id - 2) OR -- s1, s2, s3 are consecutive
        (s2.id = s1.id - 1 AND s2.id = s3.id - 2) OR -- s2, s1, s3 are consecutive (s1 is middle)
        (s3.id = s1.id - 1 AND s3.id = s2.id - 2)    -- s3, s1, s2 are consecutive (s1 is last)
    )
ORDER BY
    s1.visit_date ASC;

-- Time complexity: O(N^3) due to self-join, where N is the number of rows in the Stadium table.
-- Space complexity: O(1) for storing the result (excluding the output itself).