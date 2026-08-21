package com.pams.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pams.dto.DynamicReportRequestDTO;
import com.pams.dto.DynamicReportRequestDTONew;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class DynamicReportServiceImpl implements DynamicReportService {

    @Autowired
    private EntityManager entityManager;
    
    
    
    
    
  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public List<Object[]> getDynamicReport(DynamicReportRequestDTONew dto) {

        StringBuilder sql = new StringBuilder();

        sql.append("""
            SELECT 
                STRING_AGG(DISTINCT cact.compoundability, ', ') AS compoundability,
                STRING_AGG(DISTINCT act.act, ', ') AS acts,
                cs.pro_status,
                officer.name AS pairaviOfficer,
                officerInspector.name AS inspectorName,
                ct.court_name,
                pcd.cnr_number,
                pcd.cause_title AS case_title,
                pcd.court_case_no,
                pcd.brief,
                so.case_title,
                CONCAT(
        ud.first_name, ' ',
        COALESCE(ud.middle_name, ''), 
        CASE WHEN ud.middle_name IS NOT NULL THEN ' ' ELSE '' END,
        ud.last_name
    ) AS full_name,
hd.next_hearing_date,
stat.state
            FROM prosecution.prosecution_court_case_details pcd
            left join public.add_state as stat on stat.id=pcd.state_id
        	left join prosecution.prosecution_sanction_order_details as so on so.pro_sanction_order_id=pcd.add_case_pro_sanction_order_id
        	left join authentication.user_details as ud on ud.id=pcd.created_by_id
            LEFT JOIN authentication.court_type ct 
            ON ct.id = pcd.court_type_id

            LEFT JOIN prosecution.pairavi_details pd 
            ON pd.id = (
                SELECT MAX(id)
                FROM prosecution.pairavi_details
                WHERE procourtdtl_court_case_id = pcd.court_case_id
            )

            LEFT JOIN authentication.officers officer 
            ON officer.id = pd.pairavi_officer_id

            LEFT JOIN prosecution.inspector_details insprctor
            ON insprctor.id = (
                SELECT MAX(id)
                FROM prosecution.inspector_details
                WHERE procourtdtl_court_case_id = pcd.court_case_id
            )

            LEFT JOIN authentication.officers officerInspector 
            ON officerInspector.id = insprctor.inspector_name_id

            LEFT JOIN prosecution.pro_hearing_details hd 
            ON hd.id = (
                SELECT MAX(hd1.id)
                FROM prosecution.pro_hearing_details hd1
                WHERE hd1.procourtdtl_court_case_id = pcd.court_case_id
            )

            LEFT JOIN authentication.prosecution_status cs 
            ON cs.pro_status_id = hd.status_pro_status_id

            LEFT JOIN prosecution.charge_instace_main cm 
            ON cm.procourtdtl_court_case_id = pcd.court_case_id

            LEFT JOIN prosecution.charge_act_compund_relevant_section cact 
            ON cact.charge_instance_main_id = cm.id

            LEFT JOIN authentication.act act 
            ON act.id = cact.act_id

            WHERE 1=1
        """);

        // ✅ Dynamic Conditions

        // Case Stage
        if (dto.getStatus() != null && dto.getStatus() != 0) {
            sql.append(" AND hd.status_pro_status_id = :status ");
        }

        // Act
        if (dto.getActId() != null && dto.getActId() != 0) {
            sql.append(" AND cact.act_id = :actId ");
        }

        if (dto.getStateID() != null && dto.getStateID() != 0) {
            sql.append(" AND pcd.state_id = :id ");
        }
        
        if(dto.getCreatedBy()!=null) {
        if (dto.getCreatedBy().getId() != null && dto.getCreatedBy().getId() != 0) {
            sql.append(" AND pcd.created_by_id = :pid ");
        }
    }
        
        
        
        
        
        // Compoundability
        if (dto.getCompoundability() != null &&
            !dto.getCompoundability().equals("0")) {

            sql.append(" AND cact.compoundability = :compoundability ");
        }

        sql.append("""
            GROUP BY 
                cs.pro_status,
                officer.name,
                officerInspector.name,
                ct.court_name,
                pcd.cnr_number,
                pcd.cause_title,
                pcd.court_case_no,
                pcd.brief,
                 so.case_title,
                 CONCAT(
        ud.first_name, ' ',
        COALESCE(ud.middle_name, ''), 
        CASE WHEN ud.middle_name IS NOT NULL THEN ' ' ELSE '' END,
        ud.last_name
    ),
    hd.next_hearing_date,
    stat.state
        """);

        Query query = entityManager.createNativeQuery(sql.toString());

        // ✅ Set Parameters

        if (dto.getStatus() != null && dto.getStatus() != 0) {
            query.setParameter("status", dto.getStatus());
        }

        if (dto.getActId() != null && dto.getActId() != 0) {
            query.setParameter("actId", dto.getActId());
        }
        if (dto.getStateID() != null && dto.getStateID() != 0) {
            query.setParameter("id", dto.getStateID());
        }
        
        if(dto.getCreatedBy()!=null) {
        if (dto.getCreatedBy().getId() != null && dto.getCreatedBy().getId() != 0) {
        	  query.setParameter("pid", dto.getCreatedBy().getId());
            
        }}
        

        if (dto.getCompoundability() != null &&
            !dto.getCompoundability().equals("0") ) {

            query.setParameter("compoundability", dto.getCompoundability());
        }

        return query.getResultList();
    
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Override
    public List<Map<String, Object>> generateDynamicReport(DynamicReportRequestDTO dto) {

        StringBuilder select = new StringBuilder(
            "SELECT " +
            "so.case_title AS case_title, " +
            "so.cin_number AS cin_number"
        );

        // ✅ NULL SAFE CHECK
        if (dto.getSelectedFields() != null) {
            for (String field : dto.getSelectedFields()) {

                if ("HEARING_DETAILS".equals(field)) {
                    select.append(", hd.last_hearing_date AS last_hearing_date");
                    select.append(", hd.next_hearing_date AS next_hearing_date");
                    select.append(", hd.briefhd AS brif_hd");
                }
                else if ("COUNSEL_DETAILS".equals(field)) {
                	
                	select.append(", of.name AS officer_Name");
                	
                }
                
 else if ("PROSECUTOR_NAME".equals(field)) {
                	
                	select.append(", of.name AS officer_Name");
                	
                }
                
            }
        }

        String from =
            " FROM prosecution.prosecution_court_case_details ccd " +
            " LEFT JOIN prosecution.prosecution_sanction_order_details  " +
            " ON so.pro_sanction_order_id = ccd.add_case_pro_sanction_order_id " +
            " LEFT JOIN prosecution.pro_hearing_details hd " +
            " ON ccd.court_case_id = hd.procourtdtl_court_case_id " +
            " AND hd.latesthdstatus = true "+
            " LEFT JOIN authentication.officers of " +
            " ON hd.id = of.id " ;

        String sql = select.toString() + from;

        Query query = entityManager.createNativeQuery(sql);

        query.unwrap(org.hibernate.query.NativeQuery.class)
             .setResultTransformer(
                 org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE
             );

        return query.getResultList();
    }
    
    @Override
    public List<Map<String, Object>> generateDynamicReport1(DynamicReportRequestDTO dto) {

        StringBuilder select = new StringBuilder(
            "SELECT " +
            "so.case_title AS case_title, " +
            "so.cin_number AS cin_number"
        );

        // ✅ NULL SAFE CHECK
        if (dto.getSelectedFields() != null) {
            for (String field : dto.getSelectedFields()) {

                if ("HEARING_DETAILS".equals(field)) {
                    select.append(", hd.last_hearing_date AS last_hearing_date");
                    select.append(", hd.next_hearing_date AS next_hearing_date");
                    select.append(", hd.briefhd AS brif_hd");
                }
                else if ("COUNSEL_DETAILS".equals(field)) {
                	
                	select.append(", of.name AS officer_Name");
                	
                }
                
 else if ("PROSECUTOR_NAME".equals(field)) {
                	
                	select.append(", of.name AS officer_Name");
                	
                }
                
            }
        }

        String from =
            " FROM prosecution.prosecution_court_case_details ccd " +
            " LEFT JOIN prosecution.prosecution_sanction_order_details  " +
            " ON so.pro_sanction_order_id = ccd.add_case_pro_sanction_order_id " +
            " LEFT JOIN prosecution.pro_hearing_details hd " +
            " ON ccd.court_case_id = hd.procourtdtl_court_case_id " +
            " AND hd.latesthdstatus = true "+
            " LEFT JOIN authentication.officers of " +
            " ON hd.id = of.id " ;

        String sql = select.toString() + from;

        Query query = entityManager.createNativeQuery(sql);

        query.unwrap(org.hibernate.query.NativeQuery.class)
             .setResultTransformer(
                 org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE
             );

        return query.getResultList();
    }
































    @Override

    public List<Object[]> getDynamicReport1(DynamicReportRequestDTONew dto) {



        StringBuilder sql = new StringBuilder();



        sql.append("""

            SELECT 

                STRING_AGG(DISTINCT cact.compoundability, ', ') AS compoundability,

                STRING_AGG(DISTINCT act.act, ', ') AS acts,

                cs.pro_status,

                officer.name AS pairaviOfficer,

                officerInspector.name AS inspectorName,

                ct.court_name,

                pcd.cnr_number,

                pcd.cause_title AS case_title,

                pcd.court_case_no,

                pcd.brief,

                so.case_title,

                CONCAT(

        ud.first_name, ' ',

        COALESCE(ud.middle_name, ''), 

        CASE WHEN ud.middle_name IS NOT NULL THEN ' ' ELSE '' END,

        ud.last_name

    ) AS full_name,

hd.next_hearing_date,

stat.state,dist.district_name,bench.typeofbench

            FROM prosecution.prosecution_court_case_details pcd

            left join public.add_state as stat on stat.id=pcd.state_id

            left join public.district as dist on dist.id=pcd.city_id

            left join authentication.type_of_bench bench on bench.id=pcd.bench_name_id

        	left join prosecution.prosecution_sanction_order_details as so on so.pro_sanction_order_id=pcd.add_case_pro_sanction_order_id

        	left join authentication.user_details as ud on ud.id=pcd.created_by_id

            LEFT JOIN authentication.court_type ct 

            ON ct.id = pcd.court_type_id



            LEFT JOIN prosecution.pairavi_details pd 

            ON pd.id = (

                SELECT MAX(id)

                FROM prosecution.pairavi_details

                WHERE procourtdtl_court_case_id = pcd.court_case_id

            )



            LEFT JOIN authentication.officers officer 

            ON officer.id = pd.pairavi_officer_id



            LEFT JOIN prosecution.inspector_details insprctor

            ON insprctor.id = (

                SELECT MAX(id)

                FROM prosecution.inspector_details

                WHERE procourtdtl_court_case_id = pcd.court_case_id

            )



            LEFT JOIN authentication.officers officerInspector 

            ON officerInspector.id = insprctor.inspector_name_id



            LEFT JOIN prosecution.pro_hearing_details hd 

            ON hd.id = (

                SELECT MAX(hd1.id)

                FROM prosecution.pro_hearing_details hd1

                WHERE hd1.procourtdtl_court_case_id = pcd.court_case_id

            )



            LEFT JOIN authentication.prosecution_status cs 

            ON cs.pro_status_id = hd.status_pro_status_id



            LEFT JOIN prosecution.charge_instace_main cm 

            ON cm.procourtdtl_court_case_id = pcd.court_case_id



            LEFT JOIN prosecution.charge_act_compund_relevant_section cact 

            ON cact.charge_instance_main_id = cm.id



            LEFT JOIN authentication.act act 

            ON act.id = cact.act_id



            WHERE 1=1

        """);



        // ✅ Dynamic Conditions



        // Case Stage

        if (dto.getStatus() != null && dto.getStatus() != 0) {

            sql.append(" AND hd.status_pro_status_id = :status ");

        }



        // Act

        if (dto.getActId() != null && dto.getActId() != 0) {

            sql.append(" AND cact.act_id = :actId ");

        }



        if (dto.getStateID() != null && dto.getStateID() != 0) {

            sql.append(" AND pcd.state_id = :id ");

        }

        

        if(dto.getCreatedBy()!=null) {

        if (dto.getCreatedBy().getId() != null && dto.getCreatedBy().getId() != 0) {

            sql.append(" AND pcd.created_by_id = :pid ");

        }

    }

        

        

        	  if (dto.getCaseType() != null && dto.getCaseType() != 0) {

        		  sql.append(" AND pcd.type_id = :tid ");

        	  }

        

        

        

        

        

        // Compoundability

        if (dto.getCompoundability() != null &&

            !dto.getCompoundability().equals("0")) {



            sql.append(" AND cact.compoundability = :compoundability ");

        }



        sql.append("""

            GROUP BY 

                cs.pro_status,

                officer.name,

                officerInspector.name,

                ct.court_name,

                pcd.cnr_number,

                pcd.cause_title,

                pcd.court_case_no,

                pcd.brief,

                 so.case_title,

                 CONCAT(

        ud.first_name, ' ',

        COALESCE(ud.middle_name, ''), 

        CASE WHEN ud.middle_name IS NOT NULL THEN ' ' ELSE '' END,

        ud.last_name

    ),

    hd.next_hearing_date,

    stat.state,dist.district_name,bench.typeofbench

        """);



        Query query = entityManager.createNativeQuery(sql.toString());



        // ✅ Set Parameters



        if (dto.getStatus() != null && dto.getStatus() != 0) {

            query.setParameter("status", dto.getStatus());

        }



        if (dto.getActId() != null && dto.getActId() != 0) {

            query.setParameter("actId", dto.getActId());

        }

        if (dto.getStateID() != null && dto.getStateID() != 0) {

            query.setParameter("id", dto.getStateID());

        }

        

        if(dto.getCreatedBy()!=null) {

        if (dto.getCreatedBy().getId() != null && dto.getCreatedBy().getId() != 0) {

        	  query.setParameter("pid", dto.getCreatedBy().getId());

            

        }}

        

        if (dto.getCaseType() != null && dto.getCaseType() != 0) {

        	 query.setParameter("tid", dto.getCaseType());

        }

        



        if (dto.getCompoundability() != null &&

            !dto.getCompoundability().equals("0") ) {



            query.setParameter("compoundability", dto.getCompoundability());

        }



        return query.getResultList();

    

    }


}
