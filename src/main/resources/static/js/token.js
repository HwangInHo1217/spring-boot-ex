const token=searchParam('token')
if(token){
    localStorage.setItem("access_token",token)
}
function searchParam(key){
    return new URLSerchParams(location.search).get(key);
}