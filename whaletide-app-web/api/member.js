import request from '@/utils/requestUtil'

export function memberLogin(data) {
	return request({
		method: 'POST',
		url: '/sso/login',
		header: {
			'content-type': 'application/x-www-form-urlencoded;charset=utf-8'
		},
		data: data
	})
}

export function memberInfo() {
	return request({
		method: 'GET',
		url: '/sso/info'
	})
}

// 获取短信验证码
export function getSmsCode(mobile) {
	return request({
		method: 'GET',
		url: '/sso/sms/code',
		params: { phone: mobile }
	});
}

// 用户注册API
export function memberRegister(data) {
	return request({
		method: 'POST',
		url: '/sso/register',
		data: data
	})
}

// 更新用户信息
export function updateMemberInfo(data) {
	// 直接使用完整授权头，不尝试解析token
	const fullAuth = uni.getStorageSync('FullAuthorization');
	
	// 记录授权信息
	console.log('请求授权头:', fullAuth);
	
	// 确保有授权头
	const header = {};
	if (fullAuth) {
		header['Authorization'] = fullAuth;
	}
	
	return request({
		method: 'POST',
		url: '/member/info/update',
		data: data,
		header: header
	})
}

// 修改密码
export function updatePassword(data) {
	return request({
		method: 'POST',
		url: '/member/password/update',
		data: data
	})
}

// 上传头像
export function uploadAvatar(data) {
	return request({
		method: 'POST',
		url: '/member/avatar/upload',
		data: data
	})
}
