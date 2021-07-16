import './App.css';
import axios from 'axios';
import * as dd from 'dingtalk-jsapi';

//内网穿透工具介绍:
// https://developers.dingtalk.com/document/resourcedownload/http-intranet-penetration?pnamespace=app
// 替换成后端服务域名
export const domain = "";

function App() {

    const corpId = window.location.href.split("=")[1]

    const initData = () => {
        // 服务商数据初始化
        axios({
            url: domain + '/esign/corp?authCorpId=' + corpId,
            method: 'post',
            data: {},
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            alert(JSON.stringify(response));
        }).catch(function (error) {
            console.log(error);
        });
    }


    const uploadFile = () => {
        // 创建签署流程
        axios({
            url: domain + '/esign/corp/file?authCorpId=' + corpId,
            method: 'post',
            data: {},
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            alert(JSON.stringify(response));
            sessionStorage.setItem("fileName", response.data.data.fileName)
            sessionStorage.setItem("fileId", response.data.data.fileId)
        }).catch(function (error) {
            console.log(error);
        });
    }

    const createESignProcess = () => {
        let data = {
            "authCorpId": corpId,
            "userId": sessionStorage.getItem("userId"),
            "fileName": sessionStorage.getItem("fileName"),
            "fileId": sessionStorage.getItem("fileId")
        };
        // 创建签署流程
        axios({
            url: domain + '/esign/corp/process',
            method: 'post',
            data: data,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            alert(JSON.stringify(response));
            sessionStorage.setItem("taskId", response.data.data.taskId)
        }).catch(function (error) {
            console.log(error);
        });
    }

    const getESignProcessFiles = () => {
        // 获取签署完成的合同列表
        axios(domain + '/esign/corp/process/' + sessionStorage.getItem("taskId") + '/docs?authCorpId=' + corpId
        ).then(function (response) {
            alert(JSON.stringify(response));
        }).catch(function (error) {
            console.log(error);
        });
    }

    dd.ready(function () {
        // dd.ready参数为回调函数，在环境准备就绪时触发，jsapi的调用需要保证在该回调函数触发后调用，否则无效。
        dd.runtime.permission.requestAuthCode({
            corpId: corpId, //三方企业ID
            onSuccess: function (result) {
                // alert(JSON.stringify(result));
                axios.get(domain + "/login?corpId=" + corpId + "&authCode=" + result.code)
                    .then(response => {
                        // alert(JSON.stringify(response));
                        // alert(JSON.stringify(response.data));
                        // alert(JSON.stringify(response.data.data.userid));
                        // alert(JSON.stringify(response.data.data.deptIdList[0]));
                        // 登录成功后储存用户部门和ID
                        sessionStorage.setItem("userId", response.data.data.userid);
                        sessionStorage.setItem("deptId", response.data.data.deptIdList[0]);
                    })
                    .catch(error => {
                        alert(JSON.stringify(error))
                        // console.log(error.message)
                    })

            },
            onFail: function (err) {
                alert(JSON.stringify(err))
            }
        });
    });

    return (
        <div className="App">
            {/*<header className="App-header">*/}
            <button onClick={initData}>服务商数据初始化</button>
            <button onClick={uploadFile}>上传文件</button>
            <button onClick={createESignProcess}>创建签署流程</button>
            <button onClick={getESignProcessFiles}>获取签署完成的合同列表</button>
            {/*</header>*/}
            {/*<header className="App-header">*/}
            {/*<button onClick={}>获取提交的审批信息</button>*/}
            {/*</header>*/}
        </div>
    );
};


export default App;
