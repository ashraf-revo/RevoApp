package org.revo

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestOperations

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
class RevoFeignClientApplication {

    static void main(String[] args) {
        SpringApplication.run RevoFeignClientApplication, args
    }
}

@Component
class ServiceHelper {
    @Autowired
    @LoadBalanced
    RestOperations rest;

    @HystrixCommand(fallbackMethod = "getServiceInstancefallback")
    String getServiceInstance() {
        rest.getForObject("http://revo-service/", String.class);
    }

    String getServiceInstancefallback() {
        "error"
    }

}

@RestController
class main {
    @Autowired
    ServiceHelper serviceHelper

    @RequestMapping("/")
    def index() {
        serviceHelper.getServiceInstance()
    }

    @RequestMapping("/feign")
    def feign() {
        feignsService.getServiceInstance()
    }
    @Autowired
    FeignsService feignsService
}

@FeignClient("revo-service")
public interface FeignsService {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String getServiceInstance()
}