(()=>{let e={action:new Map};var t=new Map;function o(){var e=navigator.userAgent.toLowerCase();return!!/iphone|ipad|ipod/.test(e)}e.enable=function(){return e.request({method:"eth_requestAccounts"})},e.send=function(t,o){return console.log("ethereum.send ->",t,o),"eth_accounts"==t?e.request({method:"eth_accounts"}):"eth_requestAccounts"==t?e.request({method:"eth_requestAccounts"}):"eth_chainId"==t?e.request({method:"eth_chainId"}):"net_version"==t?e.request({method:"net_version"}):void 0},e.request=function(o){return console.log("request->",o),new Promise((function(n,s){let r="";e.isCVN&&(o.method="cvn_"+o.method),console.log("request->"+o.method,o.params),r=appEnter.callApp(o.method,o.params),console.log("result->"+r),'"promise"'==r||"promise"==r?(console.log("-------------------------- result is promise ----------------------------------------"),t.set(o.method,n)):n(r)}))},e.frAppResult=function(e,o){console.log("frAppResult->",e,o),console.log("callback->",t.get(e)),"eth_requestAccounts"==e||"eth_accounts"==e||"cvn_eth_requestAccounts"==e||"cvn_eth_accounts"==e?t.get(e)(JSON.parse(o)):t.get(e)(o)},e.isConnected=function(){return appEnter.callApp("app_isConnected")},e.on=function(t,o){e.action.set(t,o)},e.emit=function(t,o){let n=e.action.get(t);n&&n(o)},window.ethereum=e,window.appEnter={isiOSPlatform:o,callApp:function(e,t){try{console.log("callApp",e,t);let n="";return n=o()?window.webkit.messageHandlers.callAPI.postMessage({api:e,param:t}):window.frwallet.callAPI(e,JSON.stringify(t)),console.log("-----------\x3e"+n),"string"==typeof n&&(n.startsWith("{")||n.startsWith("["))&&(n=JSON.parse(n)),n}catch(e){console.log("error->",e.message)}}}})();