package io.kb.kafkastatementsservice.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.kb.kafkastatementsservice.model.TransactionInfo;

@JsonDeserialize(as = TransactionInfo.class)
// TODO explore to make it generic
public interface JSONSerdeCompatible {}
