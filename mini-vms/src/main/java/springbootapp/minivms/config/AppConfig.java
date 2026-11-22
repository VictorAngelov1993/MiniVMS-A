package springbootapp.minivms.config;

import org.modelmapper.ModelMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springbootapp.minivms.model.dto.persondto.SupplierDropDownDto;
import springbootapp.minivms.model.dto.workitemdto.timesheet.TimeSheetRowDto;
import springbootapp.minivms.model.dto.workitemdto.worker.WorkerDetailDto;
import springbootapp.minivms.model.entities.persons.Supplier;
import springbootapp.minivms.model.entities.persons.Worker;
import springbootapp.minivms.model.entities.workitems.timesheets.TimeSheet;

// Central configuration class: defines beans that are shared and reused throughout the application

@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //ModelMapper is an intelligent object-to-object mapping library for Java
    // that simplifies converting one object model to another—such as
    // mapping a database Entity to a Data Transfer Object (DTO).
    // It uses convention over configuration to automatically match fields with similar names.
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(Worker.class, WorkerDetailDto.class)
                .addMapping(Worker::getPhoneNumber, WorkerDetailDto::setPhone)
                .addMapping(Worker::getWorkOrders, WorkerDetailDto::setAssignedWorkOrders)
                .addMapping(Worker::getWorkerId, WorkerDetailDto::setId);

        mapper.typeMap(TimeSheet.class, TimeSheetRowDto.class)
                .addMapping(TimeSheet::getTimeSheetId, TimeSheetRowDto::setId);

        mapper.typeMap(Supplier.class, SupplierDropDownDto.class)
                .addMapping(Supplier::getFullName, SupplierDropDownDto::setFullName);

        return mapper;
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("buyersList", "approvedTimesheetCounts");
    }
}
