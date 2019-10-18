/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author: Willi Menapace <willi.menapace@gmail.com>
 * 
 ******************************************************************************/

package it.webapp.view;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for the management of View objects
 */
public class ViewFactory {

	private static final Map<Class<? extends View>, View> instances = new HashMap<>();
	
	static {
		//Initializes the instance map vith all View instances
		instances.put(HomepageView.class, new HomepageView());
		instances.put(ErrorView.class, new ErrorView());
		instances.put(LoginView.class, new LoginView());
		instances.put(RedirectView.class, new RedirectView());
		instances.put(UpdateShopView.class, new UpdateShopView());
		instances.put(AddShopView.class, new AddShopView());
		instances.put(AddShopSuccessfulView.class, new AddShopSuccessfulView());
		instances.put(CartView.class, new CartView());
		instances.put(UserHubView.class, new UserHubView());
		instances.put(ShopView.class, new ShopView());
		instances.put(AddIssueFormView.class, new AddIssueFormView());
		instances.put(NotificationBaseView.class, new NotificationBaseView());
		instances.put(NotificationView.class, new NotificationView());
		instances.put(ConfirmUserSuccessfulView.class, new ConfirmUserSuccessfulView());
		instances.put(ConfirmUserErrorView.class, new ConfirmUserErrorView());
		instances.put(StartResetUserPasswordView.class, new StartResetUserPasswordView());
		instances.put(StartResetUserPasswordSuccessfulView.class, new StartResetUserPasswordSuccessfulView());
		instances.put(ResetUserPasswordView.class, new ResetUserPasswordView());
		instances.put(EndResetUserPasswordSuccessfulView.class, new EndResetUserPasswordSuccessfulView());
		instances.put(UserProfileView.class, new UserProfileView());
		instances.put(UserIssueBaseView.class, new UserIssueBaseView());
		instances.put(ShopIssueBaseView.class, new ShopIssueBaseView());
		instances.put(AdminIssueBaseView.class, new AdminIssueBaseView());
		instances.put(IssueView.class, new IssueView());
		instances.put(PrivacyPolicyView.class, new PrivacyPolicyView());
		instances.put(ItemView.class, new ItemView());
		instances.put(OrderInfoView.class, new OrderInfoView());
		instances.put(UserOrdersBaseView.class, new UserOrdersBaseView());
		instances.put(ShopOrdersBaseView.class, new ShopOrdersBaseView());
		instances.put(UserOrdersView.class, new UserOrdersView());
		instances.put(ShopOrdersView.class, new ShopOrdersView());
		instances.put(AddUserSuccessfulView.class, new AddUserSuccessfulView());
		instances.put(ItemSearchHintView.class, new ItemSearchHintView());
		instances.put(OrderOptionsView.class, new OrderOptionsView());
		instances.put(PlaceOrderSuccessfulView.class, new PlaceOrderSuccessfulView());
		instances.put(UpdateOrderStatusView.class, new UpdateOrderStatusView());
		instances.put(AddShopReviewView.class, new AddShopReviewView());
		instances.put(AddItemReviewView.class, new AddItemReviewView());
		instances.put(AddIssueEvaluationView.class, new AddIssueEvaluationView());
		instances.put(ItemSearchView.class, new ItemSearchView());
		instances.put(ItemSearchBaseView.class, new ItemSearchBaseView());
		instances.put(StaticContentView.class, new StaticContentView());
		instances.put(ItemReviewView.class, new ItemReviewView());
		instances.put(ShopReviewView.class, new ShopReviewView());
		instances.put(AddUserFormView.class, new AddUserFormView());
		instances.put(UpdateShopShipmentTypeFormView.class, new UpdateShopShipmentTypeFormView());
		instances.put(MessageView.class, new MessageView());
	}
	
	private ViewFactory() {
		//Enforces non instantiability
	}
	
	public static synchronized <E extends View> E getView(Class<E> requiredClass) {
		E requiredView = (E) instances.get(requiredClass);
		if(requiredView == null) {
			throw new UnsupportedOperationException("Cannot get an instance of class " + requiredView.toString());
		}
		
		return (E) instances.get(requiredClass);
	}
	
}
