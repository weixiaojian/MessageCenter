# Swagger相关
> 这里使用的是Springfox 3.0，是一套用于RESTful API开发的工具，可以生成非常直观的接口文档用于前后端分离的接口交流。

* 引入pom
```
        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
        </dependency>
```

* 添加Swagger配置类
```
@ApiModel
@Configuration
@EnableOpenApi
public class SwaggerConfiguration {
    /**
     * 对C端用户的接口文档
     *
     * @return
     */
    @Bean
    public Docket webApiDoc() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("MessageCenter接口文档")
                .pathMapping("/")
                //定义是否开启Swagger，false是关闭，可以通过变量去控制，线上关闭
                .enable(true)
                //配置文档的元信息
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.imwj.msg.controller"))
                //正则匹配请求路径，并分配到当前项目组
                //.paths(PathSelectors.ant("/api/**"))
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MessageCenter平台")
                .description("消息推送接口接口文档")
                .contact(new Contact("langao", "https://github.com/weixiaojian/MessageCenter", "2916863213@qq.com"))
                .version("v1.0")
                .build();
    }
}
```

* controller上增加相关注解
```
@Api("发送消息")
@Slf4j
@RestController
@RequestMapping("/sms")
public class SendController {

    @Resource
    private SendService sendService;

    /**
     * 发送短信 {"code":"send","messageParam":{"receiver":"15200985202","variables":{"conten":"6666"}},"messageTemplateId":1}
     * @param sendRequest
     * @return
     */
    @ApiOperation("/发送短信")
    @GetMapping("/sendSmsTest")
    public RetResult sendSmsTest(@RequestBody SendRequest sendRequest) {
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }

    /**
     * 发送邮件 {"code":"send","messageParam":{"receiver":"2916863213@qq.com","variables":{"title":"EmailTest","content":"6666"}},"messageTemplateId":2}
     * @param sendRequest
     * @return
     */
    @ApiOperation("/发送邮件")
    @GetMapping("/sendEmailTest")
    public RetResult sendEmailTest(@RequestBody SendRequest sendRequest) {
        SendResponse sendResponse = sendService.send(sendRequest);
        return RetResult.success(sendResponse);
    }
}
```

* 访问页面：http://127.0.0.1/swagger-ui/index.html 或 http://127.0.0.1/swagger-ui/
* 注意：在Springfox3.0之前，访问地址为http://host/context-path/swagger-ui.html

* 常用注解
```
@Api：修饰整个类，描述Controller的作用 
@ApiOperation：描述一个类的一个方法，或者说一个接口 
@ApiParam：单个参数描述 
@ApiModel：用对象来接收参数 
@ApiModelProperty：用对象接收参数时，描述对象的一个字段 
@ApiResponse：HTTP响应其中1个描述 
@ApiResponses：HTTP响应整体描述 
@ApiIgnore：使用 该注解忽略这个API 
@ApiError ：发生错误返回的信息 
@ApiImplicitParam：一个请求参数 
@ApiImplicitParams：多个请求参数 
@ApiImplicitParam属性：
	paramType 查询参数类型 
		path 以地址的形式提交数据 
		query 直接跟参数完成自动映射赋值 
		body 以流的形式提交 仅支持POST 
		header 参数在request headers 里边提交 
		form 以form表单的形式提交 仅支持POST 
	dataType 参数的数据类型 只作为标志说明，并没有实际验证 Long String 
	name 接收参数名 
	value 接收参数的意义描述 
	required 参数是否必填 true 必填 false 非必填 
	defaultValue 默认值 
```