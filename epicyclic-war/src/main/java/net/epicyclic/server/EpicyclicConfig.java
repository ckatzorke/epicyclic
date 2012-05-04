package net.epicyclic.server;

import org.gatein.pc.api.PortletInvoker;
import org.gatein.pc.mc.PortletApplicationDeployer;
import org.gatein.pc.portlet.aspects.CCPPInterceptor;
import org.gatein.pc.portlet.aspects.ConsumerCacheInterceptor;
import org.gatein.pc.portlet.aspects.ContextDispatcherInterceptor;
import org.gatein.pc.portlet.aspects.EventPayloadInterceptor;
import org.gatein.pc.portlet.aspects.PortletCustomizationInterceptor;
import org.gatein.pc.portlet.aspects.ProducerCacheInterceptor;
import org.gatein.pc.portlet.aspects.RequestAttributeConversationInterceptor;
import org.gatein.pc.portlet.aspects.SecureTransportInterceptor;
import org.gatein.pc.portlet.aspects.ValveInterceptor;
import org.gatein.pc.portlet.container.ContainerPortletDispatcher;
import org.gatein.pc.portlet.container.ContainerPortletInvoker;
import org.gatein.pc.portlet.impl.state.StateConverterV0;
import org.gatein.pc.portlet.impl.state.StateManagementPolicyService;
import org.gatein.pc.portlet.impl.state.producer.PortletStatePersistenceManagerService;
import org.gatein.pc.portlet.state.consumer.ConsumerPortletInvoker;
import org.gatein.pc.portlet.state.producer.ProducerPortletInvoker;
import org.gatein.wci.ServletContainer;
import org.gatein.wci.impl.DefaultServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class EpicyclicConfig {

	@Bean
	public PortletApplicationDeployer portletApplicationDeployer() {
		PortletApplicationDeployer deployer = new PortletApplicationDeployer();
		deployer.setServletContainerFactory(servletContainerFactory());
//		deployer.setContainerPortletInvoker(containerPortletInvoker());
		return deployer;
	}

	@Bean
	public org.gatein.wci.ServletContainerFactory servletContainerFactory() {
		return DefaultServletContainerFactory.getInstance();
	}

	@Bean
	public ServletContainer servletContainer() {
		return servletContainerFactory().getServletContainer();
	}

	@Bean
	public PortletStatePersistenceManagerService portletStatePersistenceManagerService() {
		return new PortletStatePersistenceManagerService();
	}

	@Bean
	public StateManagementPolicyService stateManagementPolicyService() {
		StateManagementPolicyService stateManagementPolicyService = new StateManagementPolicyService();
		stateManagementPolicyService.setPersistLocally(true);
		return stateManagementPolicyService;
	}

	@Bean
	StateConverterV0 stateConverter() {
		return new StateConverterV0();
	}

	@Bean(name = "ContainerPortletInvoker")
	public PortletInvoker containerPortletInvoker() {

		EventPayloadInterceptor eventPayload = new EventPayloadInterceptor();
		eventPayload.setNext(new ContainerPortletDispatcher());

		RequestAttributeConversationInterceptor requestAttributeConversationInterceptor = new RequestAttributeConversationInterceptor();
		requestAttributeConversationInterceptor.setNext(eventPayload);

		CCPPInterceptor ccppInterceptor = new CCPPInterceptor();
		ccppInterceptor.setNext(requestAttributeConversationInterceptor);

		ProducerCacheInterceptor producerCacheInterceptor = new ProducerCacheInterceptor();
		producerCacheInterceptor.setNext(ccppInterceptor);

		ContextDispatcherInterceptor contextDispatcherInterceptor = new ContextDispatcherInterceptor();
		contextDispatcherInterceptor.setServletContainerFactory(servletContainerFactory());
		contextDispatcherInterceptor.setNext(producerCacheInterceptor);

		SecureTransportInterceptor secureTransportInterceptor = new SecureTransportInterceptor();
		secureTransportInterceptor.setNext(contextDispatcherInterceptor);

		ValveInterceptor valveInterceptor = new ValveInterceptor();
		valveInterceptor.setNext(secureTransportInterceptor);
		valveInterceptor.setPortletApplicationRegistry(portletApplicationDeployer());

		ContainerPortletInvoker containerPortletInvoker = new ContainerPortletInvoker();
		containerPortletInvoker.setNext(valveInterceptor);

		return containerPortletInvoker;
	}

	@Bean(name = "ConsumerPortletInvoker")
	public PortletInvoker consumerPortletInvoker() {
		ProducerPortletInvoker producerPortletInvoker = new ProducerPortletInvoker();
		producerPortletInvoker.setNext(containerPortletInvoker());
		producerPortletInvoker.setPersistenceManager(portletStatePersistenceManagerService());
		producerPortletInvoker.setStateConverter(stateConverter());
		producerPortletInvoker.setStateManagementPolicy(stateManagementPolicyService());

		PortletCustomizationInterceptor portletCustomizationInterceptor = new PortletCustomizationInterceptor();
		portletCustomizationInterceptor.setNext(producerPortletInvoker);

		ConsumerCacheInterceptor consumerCacheInterceptor = new ConsumerCacheInterceptor();
		consumerCacheInterceptor.setNext(portletCustomizationInterceptor);

		ConsumerPortletInvoker consumerPortletInvoker = new ConsumerPortletInvoker();
		consumerPortletInvoker.setNext(consumerCacheInterceptor);

		return consumerPortletInvoker;

	}

}