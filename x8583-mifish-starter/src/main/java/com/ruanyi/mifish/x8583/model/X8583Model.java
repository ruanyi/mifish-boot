package com.ruanyi.mifish.x8583.model;

import java.util.BitSet;

import com.ruanyi.mifish.x8583.annotation.BitMapField;
import com.ruanyi.mifish.x8583.annotation.FixedLenField;
import com.ruanyi.mifish.x8583.annotation.UnsizedField;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-10 10:51
 */
@Getter
@Setter
public class X8583Model {

    @FixedLenField(order = 0, length = 4, paddingByte = (byte)38)
    private String mti;

    @BitMapField(order = 1)
    private BitSet bitMap;

    @UnsizedField(order = 2, lenlen = 2)
    private String pan;

    @FixedLenField(order = 3, length = 4)
    private String pc;

    @FixedLenField(order = 4, length = 12)
    private String tcAmount;

    @FixedLenField(order = 5, length = 1)
    private String settlementAmount;

    @FixedLenField(order = 6, length = 12)
    private String cbAmount;

    @FixedLenField(order = 7, length = 14)
    private String tsDate;

    @FixedLenField(order = 8, length = 6)
    private String cbfAmount;

    @FixedLenField(order = 9, length = 6)
    private String crs;

    @FixedLenField(order = 10, length = 9)
    private String crcb;

    @FixedLenField(order = 11, length = 8)
    private String stan;

    @FixedLenField(order = 12, length = 6)
    private String tLocalTransaction;

    @FixedLenField(order = 13, length = 8)
    private String dLocalTransaction;

    @FixedLenField(order = 14, length = 8)
    private String expiration;

    @FixedLenField(order = 15, length = 8)
    private String settlement;

    @FixedLenField(order = 16, length = 6)
    private String conversion;

    @FixedLenField(order = 17, length = 6)
    private String capture;

    @FixedLenField(order = 18, length = 4)
    private String merchantsType;

    @FixedLenField(order = 19, length = 3)
    private String acquiringInstitutionCountryCode;

    @FixedLenField(order = 20, length = 1)
    private String panExtendedCountryCode;

    @FixedLenField(order = 21, length = 1)
    private String forwardingInstitutionCountryCode;

    @FixedLenField(order = 22, length = 3)
    private String pointOfServiceEntryMode;

    @FixedLenField(order = 23, length = 3)
    private String cardSequenceNumber;

    @FixedLenField(order = 24, length = 6)
    private String networkInternationalIdentifieer;

    @FixedLenField(order = 25, length = 2)
    private String pointOfserviceConditionCode;

    @FixedLenField(order = 26, length = 2)
    private String pointOfServicePinCapturecode;

    @FixedLenField(order = 27, length = 1)
    private String authorizationIdentificationRespLen;

    @FixedLenField(order = 28, length = 12)
    private String transactionFee;

    @FixedLenField(order = 29, length = 12)
    private String settlementFee;

    @FixedLenField(order = 30, length = 12)
    private String transactionProcessingFee;

    @FixedLenField(order = 31, length = 8)
    private String settlementProcessingFee;

    @UnsizedField(order = 32, lenlen = 2)
    private String acquiringInstitutionIdentCode;

    @UnsizedField(order = 33, lenlen = 2)
    private String forwardingInstitutionIdentCode;

    @UnsizedField(order = 34, lenlen = 2)
    private String panExtended;

    @UnsizedField(order = 35, lenlen = 2)
    private String track2Data;

    @UnsizedField(order = 36, lenlen = 3)
    private String track3Data;

    @FixedLenField(order = 37, length = 12)
    private String retrievalReferenceNumber;

    @FixedLenField(order = 38, length = 6)
    private String authorizationIdentificationResponse;

    @FixedLenField(order = 39, length = 6)
    private String responseCode;

    @UnsizedField(order = 40, lenlen = 2)
    private String serviceRestrictionCode;

    @FixedLenField(order = 41, length = 8)
    private String cardAcceptorTerminalIdentificacion;

    @FixedLenField(order = 42, length = 15)
    private String cardAcceptorIdentificationCode;

    @FixedLenField(order = 43, length = 40)
    private String cardAcceptorNameLocation;

    @UnsizedField(order = 44, lenlen = 2)
    private String aditionalResponseData;

    @UnsizedField(order = 45, lenlen = 2)
    private String track1Data;

    @FixedLenField(order = 46, length = 20)
    private String aditionalDataIso;

    @UnsizedField(order = 47, lenlen = 3)
    private String aditionalDataNational;

    @UnsizedField(order = 48, lenlen = 3)
    private String aditionalDataPrivate;

    @UnsizedField(order = 49, lenlen = 3)
    private String tsCurrencyCode;

    @FixedLenField(order = 50, length = 1)
    private String settlementCurrencyCode;

    @FixedLenField(order = 51, length = 16)
    private String cardholderBillingCurrencyCode;

    @FixedLenField(order = 52, length = 16)
    private String pinData;

    @FixedLenField(order = 53, length = 16)
    private String srci;

    @UnsizedField(order = 54, lenlen = 3)
    private String additionalAmounts;

    @UnsizedField(order = 55, lenlen = 3)
    private String reserved55Iso;

    @UnsizedField(order = 56, lenlen = 3)
    private String reserved56Iso;

    @UnsizedField(order = 57, lenlen = 3)
    private String reserved57Iso;

    @FixedLenField(order = 58, length = 6)
    private String reserved58National;

    @FixedLenField(order = 59, length = 8)
    private String reserved59National;

    @UnsizedField(order = 60, lenlen = 3)
    private String reserved60Private;

    @UnsizedField(order = 61, lenlen = 3)
    private String reserved61Private;

    @FixedLenField(order = 62, length = 3)
    private String reserved62Private;

    @UnsizedField(order = 63, lenlen = 3)
    private String reserved63Private;

    @FixedLenField(order = 64, length = 6)
    private String macf;

    @FixedLenField(order = 65, length = 2)
    private String bitmapExtended;

    @FixedLenField(order = 66, length = 18)
    private String settlementCode;

    @FixedLenField(order = 67, length = 2)
    private String extendedPaymentCode;

    @FixedLenField(order = 68, length = 18)
    private String receivingInstitutionCountryCode;

    @UnsizedField(order = 69, lenlen = 3)
    private String settlementInstitutionCountryCode;

    @FixedLenField(order = 70, length = 3)
    private String networkManagementInformationCode;

    @UnsizedField(order = 71, lenlen = 2)
    private String messageNumber;

    @FixedLenField(order = 72, length = 6)
    private String messageNumberLast;

    @UnsizedField(order = 73, lenlen = 3)
    private String dateAction;

    @UnsizedField(order = 74, lenlen = 3)
    private String creditsNumber;

    @FixedLenField(order = 75, length = 8)
    private String creditsReversalNumber;

    @FixedLenField(order = 76, length = 8)
    private String debitsNumber;

    @FixedLenField(order = 77, length = 6)
    private String debitsReversalNumber;

    @FixedLenField(order = 78, length = 3)
    private String transferNumber;

    @FixedLenField(order = 79, length = 8)
    private String transferReversalNumber;

    @FixedLenField(order = 80, length = 1)
    private String inquiriesNumber;

    @FixedLenField(order = 81, length = 10)
    private String authorizationNumber;

    @FixedLenField(order = 82, length = 4)
    private String creditsProcessingFeeAmount;

    @FixedLenField(order = 83, length = 6)
    private String creditsTransactionFeeAmount;

    @FixedLenField(order = 84, length = 1)
    private String debitsProcessingFeeAmount;

    @FixedLenField(order = 85, length = 20)
    private String debitsTransactionFeeAmount;

    @FixedLenField(order = 86, length = 8)
    private String creditsAmount;

    @FixedLenField(order = 87, length = 8)
    private String creditsReversalAmount;

    @FixedLenField(order = 88, length = 8)
    private String debitsAmount;

    @FixedLenField(order = 89, length = 8)
    private String debitsReversalAmount;

    @FixedLenField(order = 90, length = 42)
    private String originalDataElements;

    @FixedLenField(order = 91, length = 12)
    private String fileUpdateCode;

    @FixedLenField(order = 92, length = 12)
    private String fileSecurityCode;

    @FixedLenField(order = 93, length = 1)
    private String responseIndicator;

    @FixedLenField(order = 94, length = 16)
    private String serviceIndicator;

    @FixedLenField(order = 95, length = 8)
    private String replacementAmounts;

    @FixedLenField(order = 96, length = 8)
    private String messageSecurityCode;

    @FixedLenField(order = 97, length = 4)
    private String netSettlement;

    @FixedLenField(order = 98, length = 6)
    private String payee;

    @UnsizedField(order = 99, lenlen = 2)
    private String settlementInstitutionIdentCode;

    @UnsizedField(order = 100, lenlen = 2)
    private String receivingInstitutionIdentCode;

    @FixedLenField(order = 101, length = 2)
    private String fileName;

    @UnsizedField(order = 102, lenlen = 2)
    private String accountIdentification1;

    @UnsizedField(order = 103, lenlen = 2)
    private String accountIdentification2;

    @UnsizedField(order = 104, lenlen = 3)
    private String transactionDescription;

    @UnsizedField(order = 105, lenlen = 3)
    private String reservedISOUse105;

    @UnsizedField(order = 106, lenlen = 3)
    private String reservedISOUse106;

    @UnsizedField(order = 107, lenlen = 3)
    private String reservedISOUse107;

    @UnsizedField(order = 108, lenlen = 3)
    private String reservedISOUse108;

    @UnsizedField(order = 109, lenlen = 3)
    private String reservedISOUse109;

    @UnsizedField(order = 110, lenlen = 3)
    private String reservedISOUse110;

    @UnsizedField(order = 111, lenlen = 3)
    private String reservedISOUse111;

    @UnsizedField(order = 112, lenlen = 3)
    private String reservedISOUse112;

    @UnsizedField(order = 113, lenlen = 3)
    private String reservedISOUse113;

    @UnsizedField(order = 114, lenlen = 4)
    private String reservedISOUse114;

    @UnsizedField(order = 115, lenlen = 4)
    private String reservedISOUse115;

    @UnsizedField(order = 116, lenlen = 4)
    private String reservedISOUse116;

    @UnsizedField(order = 117, lenlen = 3)
    private String reservedISOUse117;

    @UnsizedField(order = 118, lenlen = 3)
    private String reservedISOUse118;

    @UnsizedField(order = 119, lenlen = 3)
    private String reservedISOUse119;

    @UnsizedField(order = 120, lenlen = 3)
    private String reservedISOUse120;

    @UnsizedField(order = 121, lenlen = 3)
    private String reservedISOUse121;

    @UnsizedField(order = 122, lenlen = 3)
    private String reservedISOUse122;

    @UnsizedField(order = 123, lenlen = 3)
    private String reservedISOUse123;

    @UnsizedField(order = 124, lenlen = 3)
    private String reservedISOUse124;

    @UnsizedField(order = 125, lenlen = 3)
    private String reservedISOUse125;

    @UnsizedField(order = 126, lenlen = 3)
    private String reservedISOUse126;

    @UnsizedField(order = 127, lenlen = 3)
    private String reservedISOUse127;

    @FixedLenField(order = 128, length = 8)
    private String mac2;
}
