import axios from "axios";
import { useState } from "react";

function JWTConnection_boot(){
    //변경값 렌더링 가능한 변수
    const [message, setMessage] = useState("초기값");
  
    const ajaxjwtLogin = function(){
        const id="user";
        const pw = "1111";
        axios.post(`http://localhost:8080/loginjwt/${id}/${pw}`)
        .then(function(response){ 
            setMessage("jwt토큰정보입니다 ==> "+JSON.stringify(response.data));
        });
    }

    const ajaxjwtHeaderLogin = function(){
        const id="user";
        const pw = "1111";
        axios.post(`http://localhost:8080/loginjwtheader/${id}/${pw}`)
        .then(function(response){ 
            setMessage("바디정보입니다 ==> "+JSON.stringify(response.data) 
            + "<br>헤더정보입니다 ==> "+JSON.stringify(response.headers.authorization));
            localStorage.setItem("jwtheader", JSON.stringify(response.headers.authorization));//영구적 클라이언트 저장
        });
    }

    const ajaxjwtHeadermypage = function(){
        axios.post(`http://localhost:8080/userinfo`, 
            {},
            { 
                headers:{Authorization: JSON.parse(localStorage.getItem("jwtheader")) }
            }
        )
        .then(function(response){ 
            setMessage("로그인이후입니다 ==> "+JSON.stringify(response.data) );
        });
    }

    const ajaxjwtHeaderLogout = function(){
        localStorage.removeItem("jwtheader"); //JwtTokenFilter
        axios.get(`http://localhost:8080/logout`) //JwtTokenFilter(헤더...)
        .then(function(response){ 
            setMessage("로그아웃이후입니다 ==> "+JSON.stringify(response.data) );
        });
    }

    //쿠키(쿠키생성-서버측 / 클라이언트 전송/저장 (서버 코드 구현) ) (로컬스토리지-클라이언트 코드 구현)
    const ajaxjwtCookieLogin = function(){
        axios.defaults.withCredentials = true;//서버 쿠키 정보 접근 활용 가능
        const id="user";
        const pw = "1111";
        axios.post(`http://localhost:8080/loginjwtcookie/${id}/${pw}`)
        .then(function(response){ 
            setMessage("바디정보입니다 ==> "+JSON.stringify(response.data) + " (쿠키 생성 정보를 확인해 보세요)");
        });
    }

    const ajaxjwtCookiemypage = function(){
        axios.defaults.withCredentials = true;//서버 쿠키 정보 접근 활용 가능
        axios.post(`http://localhost:8080/userinfo`) // 403(서버) - cors (리액트) 
        .then(function(response){ 
            setMessage("로그인 이후입니다 ==> "+JSON.stringify(response.data) );
        })
        .catch(error => {
            // 에러시 실행할 코드
            setMessage('로그인 이전입니다 요청 중 에러 발생:', error);
          })
        ;
    }

    const ajaxjwtCookieLogout = function(){
        axios.defaults.withCredentials = true;//서버 쿠키 정보 접근 활용 가능
        //axios.post(`http://localhost:8080/logout`)
        axios.post(`http://localhost:8080/logoutjwtcookie`)
        .then(function(response){ 
            setMessage("로그아웃 이후입니다 ==> "+JSON.stringify(response.data) );
        });
    }  
return (
    <div>
        <h3>서버랑 통신하는 중입니다.</h3>
        <h3>아래줄에 서버와의 통신 결과를 출력합니다.</h3>
        <h3 style= { {color:"white", backgroundColor:"red" } } >{message}</h3>  
    <hr/>
    <h3>JWT로그인</h3>
    <input type="button" value="JWT로그인" onClick={ajaxjwtLogin} />
    <hr/>
    <h3>JWT로그인(헤더)</h3>
    <input type="button" value="JWT로그인(헤더)" onClick={ajaxjwtHeaderLogin} />
    <input type="button" value="JWT내정보보기(헤더)" onClick={ajaxjwtHeadermypage} />
    <input type="button" value="JWT로그아웃(헤더)" onClick={ajaxjwtHeaderLogout} />
    <hr/>
    <h3>JWT로그인(쿠키)</h3>
    <input type="button" value="JWT로그인(쿠키)" onClick={ajaxjwtCookieLogin} />
    <input type="button" value="JWT내정보보기(쿠키)" onClick={ajaxjwtCookiemypage} />
    <input type="button" value="JWT로그아웃(쿠키)" onClick={ajaxjwtCookieLogout} /> 
    </div>
    
    
     );
    }
    export default JWTConnection_boot;