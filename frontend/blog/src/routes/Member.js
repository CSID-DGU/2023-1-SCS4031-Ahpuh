import React from "react";
import { useState } from "react";
import './Member.css';

function Member(){
    const [members, setMembers] = useState([]);
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [age, setAge] = useState('');
    const [gender, setGender] = useState('');
    const [birth, setBirth] = useState('');
    const [guardian, setGuardian] = useState('');
    const [checkedItems, setCheckedItems] = useState([]);

    const handleCheckboxChange = (event, item) => {
        if (event.target.checked) {
        setCheckedItems([...checkedItems, item]);
        } else {
        const updatedItems = checkedItems.filter((checkedItem) => checkedItem !== item);
        setCheckedItems(updatedItems);
        }
    };

    const addMember = (e) => {
      e.preventDefault();
  
      // 입력 값 유효성 검사
      if (name.trim() === '' || age.trim() === '' || gender.trim() === '') {
        alert('모든 필드를 입력해주세요.');
        return;
      }

      const newMember = {
        id: members.length + 1,
        name: name,
        phone: phone,
        age: age,
        gender: gender,
        birth: birth,
        guardian: guardian
      };
      setMembers([...members, newMember]);
      setName('');
      setPhone('');
      setAge('');
      setGender('');
      setBirth('');
      setGuardian('');
    };
  
    const deleteMember = (id) => {
      const updatedMembers = members.filter(member => member.id !== id);
      setMembers(updatedMembers);
    };
  
    return (
      <div>
        <h1>회원 관리 페이지</h1>
  
        <form onSubmit={addMember}>
          <input
            type="text"
            placeholder="이름"
            value={name}
            onChange={e => setName(e.target.value)}
          />
          <input
            type="tel"
            placeholder="전화번호"
            value={phone}
            onChange={e => setPhone(e.target.value)}
          />
          <input
            type="text"
            placeholder="나이"
            value={age}
            onChange={e => setAge(e.target.value)}
          />
          <input
            type="text"
            placeholder="성별"
            value={gender}
            onChange={e => setGender(e.target.value)}
          />
          <input
            type="date"
            placeholder="생년월일"
            value={birth}
            onChange={e => setBirth(e.target.value)}
          />
          <input
            type="tel"
            placeholder="보호자 연락처"
            value={guardian}
            onChange={e => setGuardian(e.target.value)}
          />
          <button type="submit">추가</button>
        </form>
  
        <table>
          <thead>
            <tr>
                <th>체크</th>
                <th>이름</th>
                <th>나이</th>
                <th>전화번호</th>
                <th>성별</th>
                <th>생년월일</th>
                <th>보호자</th>
                <th>삭제</th>
            </tr>
          </thead>
          <tbody>
            {members.map(member => (
              <tr key={member.id}>
                <td>
                    <input type="checkbox" checked={checkedItems.includes('item1')} onChange={(event) => handleCheckboxChange(event, 'item1')}/>
                </td>
                <td>{member.name}</td>
                <td>{member.phone}</td>
                <td>{member.age}</td>
                <td>{member.gender}</td>
                <td>{member.birth}</td>
                <td>{member.guardian}</td>
                <td>
                  <button onClick={() => deleteMember(member.id)}>삭제</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  };

export default Member;