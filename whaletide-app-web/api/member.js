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
