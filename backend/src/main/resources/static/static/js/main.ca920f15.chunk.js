(this.webpackJsonpfronted=this.webpackJsonpfronted||[]).push([[0],{268:function(t,e,n){"use strict";n.r(e);var i=n(7),a=n.n(i),s=n(27),o=n.n(s),r=(n(32),n(33),n(12)),c=n.n(r),d=n(13),u=n(3),l="http://wanzq.vaiwan.com";d.ready((function(){d.runtime.permission.requestAuthCode({corpId:"ding9f50b15bccd16741",onSuccess:function(t){alert(JSON.stringify(t)),c.a.get(l+"/login?authCode="+t.code).then((function(t){alert(JSON.stringify(t)),alert(JSON.stringify(t.data)),alert(JSON.stringify(t.data.result.userid)),alert(JSON.stringify(t.data.result.deptIdList[0])),sessionStorage.setItem("userId",t.data.result.userid),sessionStorage.setItem("deptId",t.data.result.deptIdList[0])})).catch((function(t){alert(JSON.stringify(t))}))},onFail:function(t){alert(JSON.stringify(t))}})}));var f=function(){return Object(u.jsx)("div",{className:"App",children:Object(u.jsx)("header",{className:"App-header",children:Object(u.jsx)("button",{onClick:function(){c()({url:l+"/process/instance",method:"post",data:{detailForms:[{textForms:[{name:"\u7269\u54c1\u540d\u79f0",value:"\u6d4b\u8bd5\u7269\u54c1\u9886\u7528-\u7535\u8111"},{name:"\u6570\u91cf",value:"3"}],name:"\u7269\u54c1\u660e\u7ec6"}],originatorUserId:"043217290519980938",textForms:[{name:"\u7269\u54c1\u7528\u9014",value:"\u6d4b\u8bd5\u7269\u54c1\u9886\u7528"},{name:"\u9886\u7528\u8be6\u60c5",value:"\u9886\u7528\u8be6\u60c51"}],deptId:1},headers:{"Content-Type":"application/json"}}).then((function(t){console.log(t)})).catch((function(t){console.log(t)}))},children:"\u9886\u7528\u5e76\u63d0\u4ea4\u5ba1\u6279"})})})},g=function(t){t&&t instanceof Function&&n.e(3).then(n.bind(null,269)).then((function(e){var n=e.getCLS,i=e.getFID,a=e.getFCP,s=e.getLCP,o=e.getTTFB;n(t),i(t),a(t),s(t),o(t)}))};o.a.render(Object(u.jsx)(a.a.StrictMode,{children:Object(u.jsx)(f,{})}),document.getElementById("root")),g()},32:function(t,e,n){},33:function(t,e,n){}},[[268,1,2]]]);
//# sourceMappingURL=main.ca920f15.chunk.js.map