package com.pams.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pams.dto.AssignTaskDTO;
import com.pams.dto.PendingTaskForApprovalDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PendingTaskForApprovalDAO {
	@Autowired
	private EntityManager entityManager;

	// By Keyraj 25.05.2023
	@SuppressWarnings({ "unchecked" })
	public List<PendingTaskForApprovalDTO> pendingApproval() {
		try {

			String sql = "select prosecution.assigned_task_puh.id,authentication.tasks.task,prosecution.prosecution_sanction_order_details.pro_sanction_file_name , \r\n"
					+ "prosecution.prosecution_sanction_order_details.case_title,prosecution.prosecution_sanction_order_details.pro_section_order_number, \r\n"
					+ "																	prosecution.prosecution_sanction_order_details.pro_sanction_date,authentication.sfio_as.sfio_as, \r\n"
					+ "																		prosecution.pro_hearing_details.approve_status a1, \r\n"
					+ "																		 prosecution.pairavi_details.approve_status a2, \r\n"
					+ "																		 prosecution.prosecution_complaint_details.approve_status a3, \r\n"
					+ "																		 prosecution.prosecution_case_company_details.approve_status a4, \r\n"
					+ "																		 prosecution.prosecution_accused_details.approve_status a5, \r\n"
					+ "																		prosecution.upload_additional_files_details.approve_status a6, \r\n"
					+ "																		 prosecution.pro_complaint_report_template.approve_status a7, \r\n"
					+ "																		 prosecution.prosecution_court_case_details.approve_status a8, \r\n"
					+ "																		c.approve_status a9,  \r\n"
					+ "																		prosecution.pro_freezer_asset_order.approval_status a10,\r\n"
					+ "																		prosecution.pro_response_respondent_details.approval_status a11\r\n"
					+ "\r\n"
					+ "																		FROM prosecution.assigned_task_puh  \r\n"
					+ "																		full JOIN prosecution.pairavi_details ON (prosecution.assigned_task_puh.id  = prosecution.pairavi_details.assigned_task_id and prosecution.pairavi_details.approve_status=1)  \r\n"
					+ "																		full JOIN authentication.sfio_as ON prosecution.assigned_task_puh.sfio_as_id  = authentication.sfio_as.id \r\n"
					+ "																		full JOIN prosecution.pro_hearing_details ON (prosecution.assigned_task_puh.id = prosecution.pro_hearing_details.assigned_task_id and prosecution.pro_hearing_details.approve_status=1) \r\n"
					+ "																		full JOIN prosecution.prosecution_sanction_order_details ON prosecution.assigned_task_puh.add_case_pro_sanction_order_id = prosecution.prosecution_sanction_order_details.pro_sanction_order_id   \r\n"
					+ "																		\r\n"
					+ "																		full JOIN prosecution.prosecution_complaint_details ON (prosecution.assigned_task_puh.id = prosecution.prosecution_complaint_details.assigned_task_id and prosecution.prosecution_complaint_details.approve_status=1)  \r\n"
					+ "																		\r\n"
					+ "																		full JOIN prosecution.prosecution_case_company_details ON (prosecution.assigned_task_puh.id = prosecution.prosecution_case_company_details.assigned_task_id and prosecution.prosecution_case_company_details.approve_status=1)  \r\n"
					+ "																		 \r\n"
					+ "																		full JOIN prosecution.pro_case_processing_details c ON ( prosecution.assigned_task_puh.id = c.assigned_task_id and c.approve_status=1) \r\n"
					+ "																		\r\n"
					+ "																		full JOIN prosecution.prosecution_accused_details ON (prosecution.assigned_task_puh.id = prosecution.prosecution_accused_details.assigned_task_id and prosecution.prosecution_accused_details.approve_status=1) \r\n"
					+ "																	\r\n"
					+ "																		full JOIN prosecution.upload_additional_files_details ON (prosecution.assigned_task_puh.id = prosecution.upload_additional_files_details.assigned_task_puhdtl_id and prosecution.upload_additional_files_details.approve_status=1)\r\n"
					+ "																		\r\n"
					+ "																		full JOIN prosecution.pro_complaint_report_template ON (prosecution.assigned_task_puh.id = prosecution.pro_complaint_report_template.assigned_task_puh_id and prosecution.pro_complaint_report_template.approve_status=1) \r\n"
					+ "																		\r\n"
					+ "																		full JOIN prosecution.prosecution_court_case_details ON (prosecution.assigned_task_puh.id = prosecution.prosecution_court_case_details.assigned_task_id and prosecution.prosecution_court_case_details.approve_status=4) \r\n"
					+ "																		\r\n"
					+ "																		full JOIN authentication.tasks ON prosecution.assigned_task_puh.create_task_id = authentication.tasks.id \r\n"
					+ "\r\n" + "																		\r\n"
					+ "																		full JOIN prosecution.pro_freezer_asset_order ON (prosecution.assigned_task_puh.id = prosecution.pro_freezer_asset_order.assigned_task_id and prosecution.pro_freezer_asset_order.approval_status=1)\r\n"
					+ "																		full JOIN prosecution.pro_response_respondent_details ON (prosecution.assigned_task_puh.id = prosecution.pro_response_respondent_details.assigned_task_id and prosecution.pro_response_respondent_details.approval_status=1)\r\n"
					+ "																		\r\n" + "\r\n" + "\r\n"
					+ " \r\n"
					+ "																		where prosecution.pro_hearing_details.approve_status='1' \r\n"
					+ "																		\r\n"
					+ "																		or prosecution.pairavi_details.approve_status='1'  \r\n"
					+ "																		 \r\n"
					+ "																		or prosecution.prosecution_complaint_details.approve_status='1' \r\n"
					+ "																		or prosecution.prosecution_case_company_details.approve_status='1' \r\n"
					+ "																		or prosecution.prosecution_accused_details.approve_status='1'  \r\n"
					+ "																		or prosecution.upload_additional_files_details.approve_status='1' \r\n"
					+ "																		\r\n"
					+ "																		or prosecution.pro_complaint_report_template.approve_status='1' \r\n"
					+ "																		or prosecution.prosecution_court_case_details.approve_status='4' \r\n"
					+ "																		or prosecution.pro_freezer_asset_order.approval_status='1'\r\n"
					+ "																		or  prosecution.pro_response_respondent_details.approval_status='1'\r\n"
					+ "\r\n"
					+ "																		or c.approve_status='1'  \r\n"
					+ "																	 \r\n"
					+ "																		group by 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18";

			Query query = entityManager.createNativeQuery(sql);

			List<Object[]> objects = query.getResultList();

			List<PendingTaskForApprovalDTO> list = new ArrayList<PendingTaskForApprovalDTO>();

			if (objects.size() > 0) {
				for (int j = objects.size() - 1; j >= 0; j--) {
					Object[] object = objects.get(j);

					PendingTaskForApprovalDTO pendingTaskForApprovalDTO = new PendingTaskForApprovalDTO();

					Long proSanctionID = (Long) object[0];
					

					String task = (String) object[1];

					String SanctionFileName = (String) object[2];
					String causeTitle = (String) object[3];

					String proSanctionOrderNo = (String) object[4];
					Date prosanctiondate = (Date) object[5];

					String sfioAs = (String) object[6];
					Object a11 = object[7];
					int a1 = 0;
					if (a11 != null) {
						a1 = (int) a11;
					}
					Object a22 = object[8];
					int a2 = 0;
					if (a22 != null) {
						a2 = (int) a22;
					}

					Object a33 = object[9];
					int a3 = 0;
					if (a33 != null) {
						a3 = (int) a33;
					}

					Object a44 = object[10];
					int a4 = 0;
					if (a44 != null) {
						a4 = (int) a44;
					}
					Object a55 = object[11];
					int a5 = 0;
					if (a55 != null) {
						a5 = (int) a55;
					}
					Object a66 = object[12];
					int a6 = 0;
					if (a66 != null) {
						a6 = (int) a66;
					}

					Object a77 = object[13];
					int a7 = 0;
					if (a77 != null) {
						a7 = (int) a77;
					}
					Object a88 = object[14];
					int a8 = 0;
					if (a88 != null) {
						a8 = (int) a88;
					}
					Object a99 = object[15];
					int a9 = 0;
					if (a99 != null) {
						a9 = (int) a99;
					}
					Object a111 = object[16];
					int a10 = 0;
					if (a111 != null) {
						a10 = (int) a111;
					}
					Object a1111 = object[17];
					int a12 = 0;
					if (a1111 != null) {
						a12 = (int) a1111;
					}

					pendingTaskForApprovalDTO.setA10freezer(a10);
					pendingTaskForApprovalDTO.setA11respondant(a12);

					pendingTaskForApprovalDTO.setA1Hearing(a1);
					pendingTaskForApprovalDTO.setA2Pairavi(a2);
					pendingTaskForApprovalDTO.setA3CompDtl(a3);
					pendingTaskForApprovalDTO.setA4CaseCompany(a4);
					pendingTaskForApprovalDTO.setA5Accused(a5);

					pendingTaskForApprovalDTO.setA6AdditionalFile(a6);
					pendingTaskForApprovalDTO.setA7ReportTemplate(a7);
					pendingTaskForApprovalDTO.setA8CourtCase(a8);
					pendingTaskForApprovalDTO.setA9CaseProsessingDate(a9);

					pendingTaskForApprovalDTO.setAsssignTaskID(proSanctionID);
					pendingTaskForApprovalDTO.setCaseTitle(causeTitle);
					pendingTaskForApprovalDTO.setProSanctionFileName(SanctionFileName);
					pendingTaskForApprovalDTO.setProSanctionDate(prosanctiondate);
					pendingTaskForApprovalDTO.setProSectionOrderNumber(proSanctionOrderNo);
					pendingTaskForApprovalDTO.setTask(task);

					pendingTaskForApprovalDTO.setSfioAs(sfioAs);

					list.add(pendingTaskForApprovalDTO);

				}

			}

			if (!(list).isEmpty())
				return list;
			else
				return list;

		} catch (NoResultException e) {

		}
		return null;

	}

	@SuppressWarnings({ "unchecked" })
	public List<PendingTaskForApprovalDTO> approveAndReject() {
		try {

			String sql = "\r\n"
					+ "select prosecution.assigned_task_puh.id,authentication.tasks.task,prosecution.prosecution_sanction_order_details.pro_sanction_file_name , prosecution.prosecution_sanction_order_details.case_title,prosecution.prosecution_sanction_order_details.pro_section_order_number,prosecution.prosecution_sanction_order_details.pro_sanction_date,authentication.sfio_as.sfio_as \r\n"
					+ "						\r\n" + "						FROM prosecution.assigned_task_puh\r\n"
					+ "						 \r\n"
					+ "						left JOIN prosecution.pairavi_details ON prosecution.assigned_task_puh.id  = prosecution.pairavi_details.assigned_task_id\r\n"
					+ "						 \r\n"
					+ "						left JOIN authentication.sfio_as ON prosecution.assigned_task_puh.sfio_as_id  = authentication.sfio_as.id\r\n"
					+ "						left JOIN prosecution.pro_hearing_details ON prosecution.assigned_task_puh.id = prosecution.pro_hearing_details.assigned_task_id \r\n"
					+ "						left JOIN prosecution.prosecution_sanction_order_details ON prosecution.assigned_task_puh.add_case_pro_sanction_order_id = prosecution.prosecution_sanction_order_details.pro_sanction_order_id \r\n"
					+ "						left JOIN prosecution.prosecution_complaint_details ON prosecution.assigned_task_puh.id = prosecution.prosecution_complaint_details.assigned_task_id\r\n"
					+ "						left JOIN prosecution.prosecution_case_company_details ON prosecution.assigned_task_puh.id = prosecution.prosecution_case_company_details.assigned_task_id \r\n"
					+ "						left JOIN prosecution.pro_case_processing_details ON prosecution.assigned_task_puh.id = prosecution.pro_case_processing_details.assigned_task_id \r\n"
					+ "						left JOIN prosecution.prosecution_accused_details ON prosecution.assigned_task_puh.id = prosecution.prosecution_accused_details.assigned_task_id \r\n"
					+ "						left JOIN prosecution.upload_additional_files_details ON prosecution.assigned_task_puh.id = prosecution.upload_additional_files_details.assigned_task_puhdtl_id \r\n"
					+ "						left JOIN prosecution.pro_complaint_report_template ON prosecution.assigned_task_puh.id = prosecution.pro_complaint_report_template.assigned_task_puh_id\r\n"
					+ "						left JOIN prosecution.prosecution_court_case_details ON prosecution.assigned_task_puh.id = prosecution.prosecution_court_case_details.assigned_task_id \r\n"
					+ "						left JOIN authentication.tasks ON prosecution.assigned_task_puh.create_task_id = authentication.tasks.id \r\n"
					+ "						where prosecution.pro_hearing_details.approve_status='2' or prosecution.pro_hearing_details.approve_status='2' \r\n"
					+ "						or prosecution.pairavi_details.approve_status='2'  or prosecution.pairavi_details.approve_status='2' \r\n"
					+ "						or prosecution.prosecution_complaint_details.approve_status='2' or prosecution.prosecution_complaint_details.approve_status='2' \r\n"
					+ "						or prosecution.prosecution_case_company_details.approve_status='2' or prosecution.prosecution_case_company_details.approve_status='3' \r\n"
					+ "						or prosecution.prosecution_accused_details.approve_status='2' or prosecution.prosecution_accused_details.approve_status='2' \r\n"
					+ "						or prosecution.upload_additional_files_details.approve_status='2' or prosecution.upload_additional_files_details.approve_status='3' \r\n"
					+ "						or prosecution.pro_complaint_report_template.approve_status='2' or prosecution.pro_complaint_report_template.approve_status='2' \r\n"
					+ "						or prosecution.prosecution_court_case_details.approve_status='2' or prosecution.prosecution_court_case_details.approve_status='2' \r\n"
					+ "						or prosecution.pro_case_processing_details.approve_status='2' or prosecution.pro_case_processing_details.approve_status='2'\r\n"
					+ "						group by 1,2,3,4,5,6,7";

			Query query = entityManager.createNativeQuery(sql);

			List<Object[]> objects = query.getResultList();

			List<PendingTaskForApprovalDTO> list = new ArrayList<PendingTaskForApprovalDTO>();

			if (objects.size() > 0) {
				for (int j = objects.size() - 1; j >= 0; j--) {
					Object[] object = objects.get(j);

					PendingTaskForApprovalDTO pendingTaskForApprovalDTO = new PendingTaskForApprovalDTO();

					//BigInteger proSanctionID = (BigInteger) object[0];
					Long proSanctionID = (Long) object[0];

					long asssignTaskID = proSanctionID;

					String task = (String) object[1];

					String SanctionFileName = (String) object[2];
					String causeTitle = (String) object[3];

					String proSanctionOrderNo = (String) object[4];
					Date prosanctiondate = (Date) object[5];

					String sfioAs = (String) object[6];

					pendingTaskForApprovalDTO.setAsssignTaskID(asssignTaskID);
					pendingTaskForApprovalDTO.setCaseTitle(causeTitle);
					pendingTaskForApprovalDTO.setProSanctionFileName(SanctionFileName);
					pendingTaskForApprovalDTO.setProSanctionDate(prosanctiondate);
					pendingTaskForApprovalDTO.setProSectionOrderNumber(proSanctionOrderNo);
					pendingTaskForApprovalDTO.setTask(task);

					pendingTaskForApprovalDTO.setSfioAs(sfioAs);

					list.add(pendingTaskForApprovalDTO);

				}

			}

			if (!(list).isEmpty())
				return list;
			else
				return list;

		} catch (NoResultException e) {

		}
		return null;

	}

}
