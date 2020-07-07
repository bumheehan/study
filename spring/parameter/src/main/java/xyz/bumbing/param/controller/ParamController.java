package xyz.bumbing.param.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.bumbing.param.vo.BodyVo;
import xyz.bumbing.param.vo.ParamVo;
import xyz.bumbing.param.vo.TotalVo;

/*
 * @RequestParam
 * @RequestBody
 * @ModelAttribute
 * 
 * */
@Controller
public class ParamController {

    /*
     * RequestBody와 ModelAttribute 동시 사용가능
     * url : http://localhost:8080/post?no=1&type=json
     * body : {
          "name" : "han",
          "age":19
        }
     * */
    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postjson(@RequestBody BodyVo vo, @ModelAttribute ParamVo pvo) {
	return vo.toString() + pvo.toString();
    }

    /*
     * Application form urlencoded 사용시 @RequestBody 사용금지 => 
     * Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported 에러발생
     * 
     * 
     * url : http://localhost:8080/post?no=1&type=json
     * body : name=한범희&age=19
     * contenttype : application/x-www-form-urlencoded
     * 
     * resp :TotalVo [name=한범희, age=19, no=1, type=json] 
     *  
     * 전체가 url로 들어가서 통합 VO에 들어감
     * */
    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String post(@ModelAttribute TotalVo vo) {
	return vo.toString();
    }

    /*
     * @ModelAttribute는 암묵적으로 없이 사용가능
     * url : http://localhost:8080/post?no=1&type=json
     * body : name=한범희&age=19
     * contenttype : application/x-www-form-urlencoded
     * 
     * resp :TotalVo [name=한범희, age=19, no=1, type=json] 
     *
     * */
    @PostMapping(value = "/post2", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String post2(TotalVo vo) {
	return vo.toString();
    }

    /*
     * 1:1 Parameter Bind : RequestParam
     * 
     * url : http://localhost:8080/post?no=1&type=json
     * body : name=한범희&age=19
     * contenttype : application/x-www-form-urlencoded
     * 
     * resp : 한범희 19 1 json
     */
    @PostMapping(value = "/post3", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String post2(@RequestParam(defaultValue = "Han", value = "name", required = true) String name,
	    @RequestParam("age") int age, @RequestParam("no") int no,
	    @RequestParam(value = "type", defaultValue = "none", required = false) String type) {
	return String.format("%s %d %d %s", name, age, no, type);
    }

    /*
     * 1:1 Parameter Bind : RequestParam
     * 
     * url : http://localhost:8080/post?no=1&type=json
     * body : {
        "name":"han",
        "age":29
    	}
     * resp: han 29 1 json
     */
    @PostMapping(value = "/post3", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String post3(@RequestBody BodyVo vo, @RequestParam("no") int no,
	    @RequestParam(value = "type", defaultValue = "none", required = false) String type) {
	return String.format("%s %s %d %s", vo.getName(), vo.getAge(), no, type);
    }

    /*
     * json 보낼시 requestparam으로되나 테스트
     * 
     * url : http://localhost:8080/post4
     * body : {
        "name":"han",
        "age":29
    	}
     * resp: 400에러
     */
    @PostMapping(value = "/post4", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String post4(@RequestParam(defaultValue = "Han", value = "name", required = true) String name,
	    @RequestParam("age") int age) {
	return String.format("%s %d", name, age);
    }

    /*
     * RequestBody없이 Json 테스트
     * 
     * url : http://localhost:8080/post4
     * body : {
        "name":"han",
        "age":29
    	}
     * resp: ParamVo [name=null, age=null]
     * 
     * Json => RequestBody 필수
     * */

    @PostMapping(value = "/post5", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String post5(BodyVo vo) {
	return vo.toString();
    }
}
