/*package com.alkenart.coffeeshop.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alkenart.coffeeshop.account.AccountService;
import com.alkenart.coffeeshop.account.UserInfo;
import com.alkenart.coffeeshop.order.AccountReadException;
import com.alkenart.coffeeshop.order.AccountWriteException;
import com.alkenart.coffeeshop.order.OrderInfo;
import com.alkenart.coffeeshop.order.OrderService;
import com.arris.wifisolution.common.mail.MailException;
import com.arris.wifisolution.common.mail.MailService;

@RequestMapping("/api/order/")
@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	AccountService accountService;

	@Autowired
	MailService mailService;

	@RequestMapping(value = "/generateAccessCodeSet", method = RequestMethod.POST)
	@ResponseBody
	public Set<OrderInfo> generateAccessCodeSet(
			@RequestParam(value = "realm") String realm,
			@RequestParam(value = "productCode") String productCode,
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "count") int count)
			throws AccountWriteException, MailException {
		Set<OrderInfo> response = null;
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		UserInfo user = accountService.getCurrentUser(auth.getName());
		response = orderService.createAccessCodeSet(realm, productCode,
				productName, count);

		if (!response.isEmpty() && !mailService.isDisabled()) {
			mailService.send(AccessCodeInfo.getAccessCodeData(response),
					"accessCodes.csv", "Access Code Details", user.getUserId(),
					user.getFirstName(), "");
		}
		return response;
	}

	@RequestMapping(value = "/generateAccessCode", method = RequestMethod.POST)
	@ResponseBody
	public Set<AccessCodeInfo> generateAccessCode(
			@RequestParam(value = "accessCode") String accessCode,
			@RequestParam(value = "realm") String realm,
			@RequestParam(value = "productCode") String productCode,
			@RequestParam(value = "productName") String productName,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate)
			throws AccountWriteException, MailException {
		Set<AccessCodeInfo> response = null;
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		UserInfo user = accountService.getCurrentUser(auth.getName());
		response = orderService.createAccessCode(accessCode, realm,
				productCode, productName, startDate, endDate);

		if (!response.isEmpty() && !mailService.isDisabled()) {
			mailService.send(AccessCodeInfo.getAccessCodeData(response),
					"accessCodes.csv", "Access Code Details", user.getUserId(),
					user.getFirstName(), "");
		}

		return response;
	}

	@RequestMapping(value = "/getAllAccessCodes", method = RequestMethod.GET)
	@ResponseBody
	public Set<AccessCodeInfo> getAllAccessCodes() throws AccountReadException {

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String userName = auth.getName();
		Set<AccessCodeInfo> response = orderService
				.getAllAccessCodes(accountService.getGroups(userName));
		return response;
	}

	@RequestMapping(value = "/getAllMasterAccounts", method = RequestMethod.GET)
	@ResponseBody
	public Set<MasterAccountInfo> getAllMasterAccounts()
			throws AccountReadException {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String userName = auth.getName();
		Set<MasterAccountInfo> response = orderService
				.getAllMasterAccounts(accountService.getGroups(userName));
		return response;
	}

	@RequestMapping(value = "/updateAccountsExpiry", method = RequestMethod.POST)
	@ResponseBody
	public Set<MasterAccountInfo> updateAccountsExpiry(
			@RequestBody Set<MasterAccountInfo> masterAccInfos,
			@RequestParam(value = "timeToExtend") String timeToExtend)
			throws AccountWriteException, MailException{
		Set<MasterAccountInfo> response = null;
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		UserInfo user = accountService.getCurrentUser(auth.getName());
		response = orderService.updateAccountsExpiry(masterAccInfos,
				timeToExtend);

		if (!response.isEmpty() && !mailService.isDisabled()) {
			mailService
					.send(MasterAccountInfo.getMasterAccData(response),
							"masterAccounts.csv", "Master Account Details",
							user.getUserId(), user.getFirstName(),
							"Please find the attached csv file for updated master account details.");
		}

		return response;
	}
	
	@RequestMapping(value = "/updateAccountsUsername", method = RequestMethod.POST)
	@ResponseBody
	public Set<MasterAccountInfo> updateAccountsUsername(
			@RequestBody Set<MasterAccountInfo> masterAccInfos)
			throws AccountWriteException, MailException, AccountReadException{
		Set<MasterAccountInfo> response = null;
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		UserInfo user = accountService.getCurrentUser(auth.getName());
		response = orderService.updateAccountsUsername(masterAccInfos);

		if (!response.isEmpty() && !mailService.isDisabled()) {
			mailService
					.send(MasterAccountInfo.getMasterAccData(response),
							"Accounts.csv", "Master Account and Subaccounts Details",
							user.getUserId(), user.getFirstName(),
							"Please find the attached csv file for updated master account and subaccount details.");
		}

		return response;
	}
}
*/