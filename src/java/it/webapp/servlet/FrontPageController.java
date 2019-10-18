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

package it.webapp.servlet;

import it.webapp.applicationcontroller.*;
import it.webapp.requestprocessor.RequestPreprocessorException;
import it.webapp.view.ErrorView;
import it.webapp.view.ViewFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Front page servlet that receives and dispatches
 * every request to the correct application controller
 */
@MultipartConfig
public class FrontPageController extends HttpServlet {

    //Prefix for the URL of every controller
    //Modify also in web.xml
    public static final String CONTROLLERS_BASE_PATH = "/controllers";
    
    //Map from request URI to ApplicationController
    private Map<String, ApplicationController> applicationControllerURIMap;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        applicationControllerURIMap = new HashMap<>();
        
        try {
            //Registers every application controller with its URI
            applicationControllerURIMap.put(AddIssueController.URL, new AddIssueController());
            applicationControllerURIMap.put(AddIssueEvaluationController.URL, new AddIssueEvaluationController());
            applicationControllerURIMap.put(AddIssueEvaluationFormController.URL, new AddIssueEvaluationFormController());
            applicationControllerURIMap.put(AddItemReviewController.URL, new AddItemReviewController());
            applicationControllerURIMap.put(AddShopController.URL, new AddShopController());
            applicationControllerURIMap.put(AddShopFormController.URL, new AddShopFormController());
            applicationControllerURIMap.put(AddShopReviewController.URL, new AddShopReviewController());
            applicationControllerURIMap.put(AddUserController.URL, new AddUserController());
            applicationControllerURIMap.put(AddUserFormController.URL, new AddUserFormController());
            applicationControllerURIMap.put(AdminIssueBaseController.URL, new AdminIssueBaseController());
            applicationControllerURIMap.put(AdminIssueController.URL, new AdminIssueController());
            applicationControllerURIMap.put(CartController.URL, new CartController());
            applicationControllerURIMap.put(ConfirmUserController.URL, new ConfirmUserController());
            applicationControllerURIMap.put(EndResetUserPasswordController.URL, new EndResetUserPasswordController());
			ApplicationController homepageController = new HomepageController();
            applicationControllerURIMap.put("", homepageController); //The empty url is mapped to the homepage
            applicationControllerURIMap.put("/", homepageController); //The empty url is mapped to the homepage
            applicationControllerURIMap.put(HomepageController.URL, homepageController);
            applicationControllerURIMap.put(AddIssueFormController.URL, new AddIssueFormController());
            applicationControllerURIMap.put(ItemController.URL, new ItemController());
            applicationControllerURIMap.put(ItemReviewController.URL, new ItemReviewController());
            applicationControllerURIMap.put(ItemSearchBaseController.URL, new ItemSearchBaseController());
            applicationControllerURIMap.put(ItemSearchController.URL, new ItemSearchController());
            applicationControllerURIMap.put(ItemSearchHintController.URL, new ItemSearchHintController());
            applicationControllerURIMap.put(LoginController.URL, new LoginController());
            applicationControllerURIMap.put(LoginFormController.URL, new LoginFormController());
            applicationControllerURIMap.put(LogoutController.URL, new LogoutController());
            applicationControllerURIMap.put(NotificationBaseController.URL, new NotificationBaseController());
            applicationControllerURIMap.put(NotificationController.URL, new NotificationController());
            applicationControllerURIMap.put(OrderInfoController.URL, new OrderInfoController());
            applicationControllerURIMap.put(OrderOptionsController.URL, new OrderOptionsController());
            applicationControllerURIMap.put(PlaceOrderController.URL, new PlaceOrderController());
            applicationControllerURIMap.put(PrivacyPolicyController.URL, new PrivacyPolicyController());
            applicationControllerURIMap.put(ResetUserPasswordController.URL, new ResetUserPasswordController());
            applicationControllerURIMap.put(ShopController.URL, new ShopController());
            applicationControllerURIMap.put(ShopReviewController.URL, new ShopReviewController());
            applicationControllerURIMap.put(ShopIssueBaseController.URL, new ShopIssueBaseController());
            applicationControllerURIMap.put(ShopIssueController.URL, new ShopIssueController());
            applicationControllerURIMap.put(ShopOrdersBaseController.URL, new ShopOrdersBaseController());
            applicationControllerURIMap.put(ShopOrdersController.URL, new ShopOrdersController());
            applicationControllerURIMap.put(StartResetUserPasswordController.URL, new StartResetUserPasswordController());
            applicationControllerURIMap.put(StartResetUserPasswordFormController.URL, new StartResetUserPasswordFormController());
            applicationControllerURIMap.put(StaticContentController.URL, new StaticContentController());
            applicationControllerURIMap.put(UpdateCartItemQuantityController.URL, new UpdateCartItemQuantityController());
            applicationControllerURIMap.put(UpdateOrderStatusController.URL, new UpdateOrderStatusController());
			applicationControllerURIMap.put(UpdateOrderStatusFormController.URL, new UpdateOrderStatusFormController());
            applicationControllerURIMap.put(UpdateShopController.URL, new UpdateShopController());
            applicationControllerURIMap.put(UpdateShopFormController.URL, new UpdateShopFormController());	
            applicationControllerURIMap.put(UpdateShopShipmentTypeFormController.URL, new UpdateShopShipmentTypeFormController());
            applicationControllerURIMap.put(UpdateShopShipmentTypeController.URL, new UpdateShopShipmentTypeController());
            applicationControllerURIMap.put(UpdateUserController.URL, new UpdateUserController());
            applicationControllerURIMap.put(UserHubController.URL, new UserHubController());
            applicationControllerURIMap.put(UserIssueBaseController.URL, new UserIssueBaseController());
            applicationControllerURIMap.put(UserIssueController.URL, new UserIssueController());
            applicationControllerURIMap.put(UserOrdersBaseController.URL, new UserOrdersBaseController());
            applicationControllerURIMap.put(UserOrdersController.URL, new UserOrdersController());
            applicationControllerURIMap.put(UserProfileController.URL, new UserProfileController());
            
        } catch(ApplicationControllerException | RequestPreprocessorException e) {
            throw new ServletException("Could not initialize all application controllers", e);
        }
    }
    
    /**
     * Processes both GET and POST requests
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        /**
         * In case case of first visin insert the default authentication object
         */
        HttpSession session = request.getSession();
        AuthenticationInfo authInfo = (AuthenticationInfo) session.getAttribute(AuthenticationInfo.SESSION_ATTRIBUTE);
        if(authInfo == null) {
            authInfo = new NotAuthenticatedUserInfo();
            session.setAttribute(AuthenticationInfo.SESSION_ATTRIBUTE, authInfo);
        }
        
        String requestURI = request.getRequestURI(); 
        requestURI = requestURI.substring(request.getContextPath().length()); //Removes the context path
        
        if(applicationControllerURIMap.containsKey(requestURI)) {
            ApplicationController applicationController = applicationControllerURIMap.get(requestURI);
            
            applicationController.dispatch(request, response);
        } else {
            ErrorView.setRequestParameters("Resource not found", "Requested URI " + requestURI + " does not match any resource on this server", request);
            ErrorView view = ViewFactory.getView(ErrorView.class);
            view.view(request, response);
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
