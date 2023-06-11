import React from "react";
import { useState } from "react";
import '../styles/Setting.css';

function Setting(){
    const [selectedImage1, setSelectedImage1] = useState(null);
    const [selectedImage2, setSelectedImage2] = useState(null);
    const [selectedImage3, setSelectedImage3] = useState(null);
    const [points1, setPoints1] = useState([]);
    const [points2, setPoints2] = useState([]);
    const [points3, setPoints3] = useState([]);
    const [line, setLine] = useState(1);
    
    const handleLineChange = (e) => {
      setLine(e.target.value);
      console.log(line);
    }


    const handleImageChange1 = (e) => {
      const file = e.target.files[0];
      const reader = new FileReader();
  
      reader.onload = (event) => {
        setSelectedImage1(event.target.result);
      };
  
      if (file) {
        reader.readAsDataURL(file);
        handleImageUpload1(file);
      }
    };
  
    const handleImageUpload1 = (file) => {
      // 서버로 파일 업로드 요청을 보내는 로직을 작성합니다.
      // 예시로는 fetch를 사용하여 POST 요청을 보내고, 응답을 처리합니다.
      const formData = new FormData();
      formData.append('image', file);
  
      fetch('/api/upload', {
        method: 'POST',
        body: formData
      })
        .then(response => response.json())
        .then(data => {
          // 업로드 후의 처리 로직을 작성합니다.
          console.log('업로드 완료:', data);
          // 추가적인 작업을 수행하거나, 서버로부터 받은 데이터를 활용할 수 있습니다.
        })
        .catch(error => {
          console.error('업로드 실패:', error);
          // 실패 시에 대한 오류 처리 로직을 작성합니다.
        });
    };
    const handleImageChange2 = (e) => {
        const file = e.target.files[0];
        const reader = new FileReader();
    
        reader.onload = (event) => {
          setSelectedImage2(event.target.result);
        };
    
        if (file) {
          reader.readAsDataURL(file);
          handleImageUpload2(file);
        }
    };
    
    const handleImageUpload2 = (file) => {
        // 서버로 파일 업로드 요청을 보내는 로직을 작성합니다.
        // 예시로는 fetch를 사용하여 POST 요청을 보내고, 응답을 처리합니다.
        const formData = new FormData();
        formData.append('image', file);
    
        fetch('/api/upload', {
          method: 'POST',
          body: formData
        })
          .then(response => response.json())
          .then(data => {
            // 업로드 후의 처리 로직을 작성합니다.
            console.log('업로드 완료:', data);
            // 추가적인 작업을 수행하거나, 서버로부터 받은 데이터를 활용할 수 있습니다.
          })
          .catch(error => {
            console.error('업로드 실패:', error);
            // 실패 시에 대한 오류 처리 로직을 작성합니다.
          });
    };

    const handleImageChange3 = (e) => {
        const file = e.target.files[0];
        const reader = new FileReader();
    
        reader.onload = (event) => {
          setSelectedImage3(event.target.result);
        };
    
        if (file) {
          reader.readAsDataURL(file);
          handleImageUpload3(file);
        }
    };
    
    const handleImageUpload3 = (file) => {
        // 서버로 파일 업로드 요청을 보내는 로직을 작성합니다.
        // 예시로는 fetch를 사용하여 POST 요청을 보내고, 응답을 처리합니다.
        const formData = new FormData();
        formData.append('image', file);
    
        fetch('/api/upload', {
          method: 'POST',
          body: formData
        })
          .then(response => response.json())
          .then(data => {
            // 업로드 후의 처리 로직을 작성합니다.
            console.log('업로드 완료:', data);
            // 추가적인 작업을 수행하거나, 서버로부터 받은 데이터를 활용할 수 있습니다.
          })
          .catch(error => {
            console.error('업로드 실패:', error);
            // 실패 시에 대한 오류 처리 로직을 작성합니다.
          });
    };
  
    const handleImageClick1 = (e) => {
        const image = e.target;
        const imageRect = image.getBoundingClientRect();
        const offsetX = e.clientX - imageRect.left;
        const offsetY = e.clientY - imageRect.top;

        if (points1.length < 2*line) {
          const updatedPoints = [...points1, { x: offsetX, y: offsetY }];
          setPoints1(updatedPoints);
        } else {
          setPoints1([{ x: offsetX, y: offsetY }]);
        }
    };
    
    const handleImageClick2 = (e) => {
        const image = e.target;
        const imageRect = image.getBoundingClientRect();
        const offsetX = e.clientX - imageRect.left;
        const offsetY = e.clientY - imageRect.top;
    
        if (points2.length < 2*line) {
          const updatedPoints = [...points2, { x: offsetX, y: offsetY }];
          setPoints2(updatedPoints);
        } else {
          setPoints2([{ x: offsetX, y: offsetY }]);
        }
    };
    const handleImageClick3 = (e) => {
        const image = e.target;
        const imageRect = image.getBoundingClientRect();
        const offsetX = e.clientX - imageRect.left;
        const offsetY = e.clientY - imageRect.top;
    
        if (points3.length <  2*line) {
          const updatedPoints = [...points3, { x: offsetX, y: offsetY }];
          setPoints3(updatedPoints);
        } else {
          setPoints3([{ x: offsetX, y: offsetY }]);
        }
    };
    const repeat = (line, point) => {
      const arr = [];
      const head = [];
      const body = [];
      head.push(
          <thead>
            <tr>
                <th>line</th>
                <th>x1</th>
                <th>y1</th>
                <th>x2</th>
                <th>y2</th>
            </tr>
          </thead>
      )
      for (let i = 0;i < line;i++ ){
        body.push(
          <tbody>
            <td>{i+1}번 레인</td>
            <td>{point[2*i].x}</td>
            <td>{point[2*i].y}</td>
            <td>{point[2*i+1].x}</td>
            <td>{point[2*i+1].y}</td>
          </tbody>
        )
      }
      arr.push(
        <table>
          {head}
          {body}
        </table>
      )
      return arr;
    };
  
    return (
      <div>
        <h1>수영장 CCTV 설정</h1>
        <div style={{display:"inline-block"}}>
          <h2 style={{diplay: "float"}}>라인 개수를 입력하세요 : </h2>
          <div className="form login"style={{diplay: "float"}}>
                <div class="input-group">
                  <i class='bx bx-mail-send'></i>
                  <input type="number" placeholder="개수" value={line} onChange={(e)=>{setLine(e.target.value);}}/>
                </div>
              </div>
        </div>
        <div>
            <h2>1번 화면</h2>
            <div >
                {selectedImage1 && 
                <img src={selectedImage1} alt="업로드된 이미지"  onClick={handleImageClick1} style={{ cursor: 'crosshair' }}/>}
            </div>
            <div>
                {points1.length === 2*line && (
                <div>
                    {repeat(line,points1)}
                </div>
                )}
            </div>
            <input type="file" accept="image/*" onChange={handleImageChange1} />
        </div>
        <div>
            <h2>2번 화면</h2>
            <div >
                {selectedImage2 && 
                <img src={selectedImage2} alt="업로드된 이미지"  onClick={handleImageClick2} style={{ cursor: 'crosshair' }}/>}
            </div>
            <div>
                {points2.length === 2*line && (
                <div>
                    {repeat(line,points2)}
                </div>
                )}
            </div>
            <input type="file" accept="image/*" onChange={handleImageChange2} />
        </div>
        <div>
            <h2>3번 화면</h2>
            <div>
                {selectedImage3 && 
                <img src={selectedImage3} alt="업로드된 이미지" onClick={handleImageClick3} style={{ cursor: 'crosshair' }}/>}
                </div>
                <div>
                    {points3.length === 2*line && (
                    <div>
                      {repeat(line,points3)}
                    </div>
                    )}
                </div>
            </div>    
            <input type="file" accept="image/*" onChange={handleImageChange3} />
        </div>
    );
  };
  
export default Setting;