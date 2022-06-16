# WSO2 EI / ESB / AM 汉化
解压jar包，读取jar包里的Resource.properties文件，读取到配置文件内容，请求百度翻译API，获取翻译后的内容
把翻译后的内容更新到Resource.properties文件，然后再把解压后的文件夹压缩成jar包

# baiduApiTranlate.json
baiduApiTranlate.json 是百度翻译的结果

# terminology.json
terminology.json 是自定义翻译，如果觉得百度翻译不准确，或者百度翻译得奇奇怪怪的，在json文件里新增一个自定义翻译即可

# 帮助
由于翻译用的是百度翻译开放平台的api，所以有些地方可能翻译得奇奇怪怪。可以给我邮箱发邮件（邮箱：wxhntmy@163.com）提bug去修复。