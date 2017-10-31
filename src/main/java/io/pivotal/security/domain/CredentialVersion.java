package io.pivotal.security.domain;

import io.pivotal.security.data.CredentialVersionDataService;
import io.pivotal.security.entity.Credential;
import io.pivotal.security.entity.CredentialVersionData;
import io.pivotal.security.entity.EncryptedValue;
import io.pivotal.security.request.GenerationParameters;

import java.time.Instant;
import java.util.UUID;

public abstract class CredentialVersion<Z extends CredentialVersion> {

  protected CredentialVersionData delegate;
  protected Encryptor encryptor;

  public CredentialVersion(CredentialVersionData delegate) {
    this.delegate = delegate;
  }

  public abstract String getCredentialType();

  public abstract void rotate();

  public Object getValue() {
    return encryptor.decrypt(delegate.getEncryptedValueData());
  }

  public Z setValue(String value) {
    final EncryptedValue encryption = encryptor.encrypt(value);
    delegate.setEncryptedValueData(encryption);
    return (Z) this;
  }

  public UUID getUuid() {
    return delegate.getUuid();
  }

  public Z setUuid(UUID uuid) {
    delegate.setUuid(uuid);
    return (Z) this;
  }

  public String getName() {
    return delegate.getCredential().getName();
  }

  public Instant getVersionCreatedAt() {
    return delegate.getVersionCreatedAt();
  }

  public Z setVersionCreatedAt(Instant versionCreatedAt) {
    delegate.setVersionCreatedAt(versionCreatedAt);
    return (Z) this;
  }

  public Z setEncryptor(Encryptor encryptor) {
    this.encryptor = encryptor;
    return (Z) this;
  }

  public <Z extends CredentialVersion> Z save(CredentialVersionDataService credentialVersionDataService) {
    return (Z) credentialVersionDataService.save(delegate);
  }

  public Credential getCredential() {
    return delegate.getCredential();
  }

  protected void copyNameReferenceFrom(CredentialVersion credentialVersion) {
    this.delegate.setCredential(credentialVersion.delegate.getCredential());
  }

  public void createName(String name) {
    delegate.setCredential(new Credential(name));
  }

  public GenerationParameters getGenerationParameters() {
    return null;
  }

  public abstract boolean matchesGenerationParameters(GenerationParameters generationParameters);

  public void setTransitional(boolean transitional) {
    delegate.setTransitional(transitional);
  }

  public boolean isTransitional() {
    return delegate.isTransitional();
  }
}
