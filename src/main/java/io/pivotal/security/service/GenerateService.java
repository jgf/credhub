package io.pivotal.security.service;

import io.pivotal.security.audit.EventAuditRecordParameters;
import io.pivotal.security.auth.UserContext;
import io.pivotal.security.request.AccessControlEntry;
import io.pivotal.security.request.BaseCredentialGenerateRequest;
import io.pivotal.security.request.BaseCredentialSetRequest;
import io.pivotal.security.request.PasswordSetRequest;
import io.pivotal.security.request.StringGenerationParameters;
import io.pivotal.security.view.CredentialView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerateService {

  private final GeneratorService generatorService;
  private final SetService setService;

  @Autowired
  public GenerateService(GeneratorService generatorService, SetService setService) {
    this.generatorService = generatorService;
    this.setService = setService;
  }

  public CredentialView performGenerate(
      UserContext userContext,
      List<EventAuditRecordParameters> parametersList,
      BaseCredentialGenerateRequest requestBody,
      AccessControlEntry currentUserAccessControlEntry) {

    BaseCredentialSetRequest setRequest = requestBody.generateSetRequest(generatorService);

    StringGenerationParameters generationParameters = null;
    if (setRequest instanceof PasswordSetRequest) {
      generationParameters = ((PasswordSetRequest) setRequest).getGenerationParameters();
    }

    return setService.performSet(userContext,
        parametersList,
        setRequest.getName(),
        setRequest.isOverwrite(),
        setRequest.getType(),
        generationParameters,
        setRequest.getCredentialValue(),
        setRequest.getAccessControlEntries(),
        currentUserAccessControlEntry);
  }
}
