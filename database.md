## Database Interim Check

----
### Topic
#### # Fitness PT management System

-----
### Progress

###### - Database connect

###### - Data select by id

result
<img src = "result.png">

------

### Problem

###### - data select by names

- 이름으로 회원을 조회하고, 동명이인이 있는 경우 해당하는 회원의 정보를 행으로 출력.
<br>

- 또한 삽입 삭제에서도 이름으로 선택하고 중복된 데이터가 존재할 시 행을 출력하여 선택 할 수 있도록.
  - 다음과 같은 방식으로 하면 첫번째 데이터만 변수에 삽입이 됨.
```sql
EXEC SQL select m_id,m_name, reg_date into :member_id, :mem_name
	from members
	where m_id = :input_mem_id;
```

###### - input data from values
- 데이터 삽입시 주키(id)를 제외한 데이터를 입력하여 데이터를 삽입하도록.
  - e.g) input member name : jihun
    - input member birth : 980704
    - input member reg-date : 2019-12-02
    - complete.
  - Database

m_id|m_name|m_birth|m_reg_date
-|-|-|-|
01|jihun|980704|2019-12-02

이때, m_id의 제약조건으로 auto increment를 사용하여 삽입하고자 함.
