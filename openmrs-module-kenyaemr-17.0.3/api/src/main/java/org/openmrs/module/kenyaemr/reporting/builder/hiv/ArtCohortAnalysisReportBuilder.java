/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.reporting.builder.hiv;

import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAttributeType;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.hiv.IPTStartDateCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.DateARV1Calculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.InitialArtRegimenCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.LastCd4Calculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.PatientArtOutComeCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.ViralLoadResultCalculation;
import org.openmrs.module.kenyaemr.calculation.library.mchcs.PersonAddressCalculation;
import org.openmrs.module.kenyaemr.calculation.library.rdqa.DateOfFirstCTXCalculation;
import org.openmrs.module.kenyaemr.calculation.library.rdqa.HeightAtArtStartDateCalculation;
import org.openmrs.module.kenyaemr.calculation.library.rdqa.WeightAtArtStartDateCalculation;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.metadata.TbMetadata;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.ArtCohortStartMonthYearDateConverter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.CurrentCd4Converter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.DateArtStartDateConverter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.HeightConverter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.RDQACalculationResultConverter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.RDQASimpleObjectRegimenConverter;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.WeightConverter;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.ARTRegisterCohortDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.IdentifierConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.ObsMonthYearConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.TBScreeningConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.TBScreeningAtLastVisitDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.anc.EDCandANCNumberPreg1DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.anc.EDCandANCNumberPreg2DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.anc.EDCandANCNumberPreg3DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.ARTFirstSubstitutionDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.ARTFirstSwitchDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.ARTSecondSubstitutionDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.ARTSecondSwitchDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.HTSDiscordanceArtDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.PopulationTypeArtDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.TbStartDateArtDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.WHOStageArtDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.art.WeightAtArtDataDefinition;
import org.openmrs.module.kenyaemr.reporting.library.ETLReports.MOH731Greencard.ETLMoh731GreenCardIndicatorLibrary;
import org.openmrs.module.kenyaemr.reporting.library.ETLReports.art.ETLArtRegisterIndicatorLibrary;
import org.openmrs.module.kenyaemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.kenyaemr.reporting.library.shared.hiv.HivIndicatorLibrary;
import org.openmrs.module.kenyaemr.reporting.library.shared.hiv.art.ArtCohortLibrary;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.AgeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.BirthdateDataDefinition;
import org.openmrs.module.reporting.data.person.definition.ConvertedPersonDataDefinition;
import org.openmrs.module.reporting.data.person.definition.GenderDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PersonAttributeDataDefinition;
import org.openmrs.module.reporting.data.person.definition.PreferredNameDataDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by codehub on 11/06/15.
 */
@Component
@Builds({"kenyaemr.hiv.report.art.cohort.analysis.art.6","kenyaemr.hiv.report.art.cohort.analysis.art.12","kenyaemr.hiv.report.art.cohort.analysis.art.24","kenyaemr.hiv.report.art.cohort.analysis.art.36","kenyaemr.hiv.report.art.cohort.analysis.art.48","kenyaemr.hiv.report.art.cohort.analysis.art.60"})
public class ArtCohortAnalysisReportBuilder extends AbstractHybridReportBuilder {

    @Autowired
    private ArtCohortLibrary artCohortLibrary;
    @Autowired
    private CommonDimensionLibrary commonDimensions;

    @Autowired
    private HivIndicatorLibrary hivIndicators;

    @Autowired
    private ETLArtRegisterIndicatorLibrary etlArtIndicators;

    @Autowired
    private ETLMoh731GreenCardIndicatorLibrary moh731GreencardIndicators;

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     *
     * @see org.openmrs.module.kenyacore.report.builder.AbstractCohortReportBuilder#addColumns(org.openmrs.module.kenyacore.report.CohortReportDescriptor, org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition)
     */

    @Override
    protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
        return artCohortPatients();
//        Integer period = Integer.parseInt(descriptor.getId().split("\\.")[7]);
//        CohortDefinition cd = artCohortLibrary.netCohortMonthsBetweenDatesGivenMonths(period);
//        return ReportUtils.map(cd, "startDate=${startDate},endDate=${endDate}");
    }

    protected Mapped<CohortDefinition> artCohortPatients() {
        CohortDefinition cd = new ARTRegisterCohortDefinition();
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setName("ArtCohortPatients");
        return ReportUtils.map(cd, "startDate=${startDate},endDate=${endDate}");
    }

//    @Override
//    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {
//
//        return Arrays.asList(
//                   ReportUtils.map(artCohortSummaryDataset(), "startDate=${startDate},endDate=${endDate}")
//        );
//    }


     @Override
    protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
        return Arrays.asList(
                new Parameter("startDate", "Start Date", Date.class),
                new Parameter("endDate", "End Date", Date.class)
               // new Parameter("dateBasedReporting", "", String.class)  //Determines whether to add start and end date
        );
    }
    ColumnParameters children_0_to_9 = new ColumnParameters(null, "0-9", "age=0-9");
    ColumnParameters adult_10_to_19 = new ColumnParameters(null, "10-19", "age=10-19");
    ColumnParameters adult_20_and_above = new ColumnParameters(null, "20+", "age=20+");
    ColumnParameters colTotal = new ColumnParameters(null, "Total", "");

    List<ColumnParameters> artCohortAgeDisaggregation = Arrays.asList(children_0_to_9,  adult_10_to_19 , adult_20_and_above, colTotal);


    protected void addColumns(HybridReportDescriptor report, PatientDataSetDefinition dsd) {

        dsd.addSortCriteria("DOBAndAge", SortCriteria.SortDirection.DESC);
        dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Date", Date.class));

        PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class, HivMetadata._PatientIdentifierType.UNIQUE_PATIENT_NUMBER);
        PatientIdentifierType tbRegNo = MetadataUtils.existing(PatientIdentifierType.class, TbMetadata._PatientIdentifierType.DISTRICT_REG_NUMBER);
        DataDefinition identifierDef_upn = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), new IdentifierConverter());
        DataDefinition identifierDef_tb = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), tbRegNo), new IdentifierConverter());
        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
        PersonAttributeType phoneNumber = MetadataUtils.existing(PersonAttributeType.class, CommonMetadata._PersonAttributeType.TELEPHONE_CONTACT);

        dsd.setName("artCohortRegister");
        dsd.addColumn("id", new PatientIdDataDefinition(), "");
        dsd.addColumn("ART Start Date", new CalculationDataDefinition("ART Start Date", new DateARV1Calculation()), "", new CalculationResultConverter());
        dsd.addColumn("UPN", identifierDef_upn, "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("DOB", new BirthdateDataDefinition(), "", new BirthdateConverter());
        dsd.addColumn("Age", new AgeDataDefinition(), "");
        dsd.addColumn("Telephone No", new PersonAttributeDataDefinition(phoneNumber), "");
        dsd.addColumn("Village_Estate_Landmark", new CalculationDataDefinition("Village/Estate/Landmark", new PersonAddressCalculation()), "", new RDQACalculationResultConverter());
        dsd.addColumn("Population Type", new PopulationTypeArtDataDefinition(), "");
        dsd.addColumn("Discordance", new HTSDiscordanceArtDataDefinition(),"");
        dsd.addColumn("First WHO Stage", new WHOStageArtDataDefinition(), "");
        dsd.addColumn("Latest CD4", currentCd4Count(report), "onDate=${endDate}", new CurrentCd4Converter("value"));
        dsd.addColumn("Height at Art Start", new CalculationDataDefinition("Height at Art Start", new HeightAtArtStartDateCalculation()), "", new HeightConverter());
        dsd.addColumn("Weight at Art Start", new CalculationDataDefinition("Weight at Art Start", new WeightAtArtStartDateCalculation()), "", new WeightConverter());
        dsd.addColumn("CTX Start Date", new CalculationDataDefinition("Weight at Art Start", new DateOfFirstCTXCalculation()), "", new ArtCohortStartMonthYearDateConverter());
        dsd.addColumn("IPT Start Date", new CalculationDataDefinition("IPT Start Date", new IPTStartDateCalculation()), "", new DateArtStartDateConverter());
        dsd.addColumn("TBRx Start Date", new TbStartDateArtDataDefinition(), "", new ObsMonthYearConverter());
        dsd.addColumn("TB Reg", identifierDef_tb, "");
        dsd.addColumn("Pregnancy_1", new EDCandANCNumberPreg1DataDefinition(),"");
        dsd.addColumn("Pregnancy_2", new EDCandANCNumberPreg2DataDefinition(),"");
        dsd.addColumn("Pregnancy_3", new EDCandANCNumberPreg3DataDefinition(),"");
        dsd.addColumn("Initial ART Regimen", new CalculationDataDefinition("Initial ART Regimen", new InitialArtRegimenCalculation()), "", null);
        dsd.addColumn("ART First Sub", new ARTFirstSubstitutionDataDefinition(),"");
        dsd.addColumn("ART Second Sub", new ARTSecondSubstitutionDataDefinition(),"");
        dsd.addColumn("ART First Switch", new ARTFirstSwitchDataDefinition(),"");
        dsd.addColumn("ART Second Switch", new ARTSecondSwitchDataDefinition(),"");
        dsd.addColumn("Weight", new WeightAtArtDataDefinition(), "");
        dsd.addColumn("Recent Viral Load Result", new CalculationDataDefinition("Recent Viral Load Result", new ViralLoadResultCalculation("last")), "", new RDQASimpleObjectRegimenConverter("data"));
        dsd.addColumn("Recent Viral Load Result Date", new CalculationDataDefinition("Recent Viral Load Result Date", new ViralLoadResultCalculation("last")), "", new RDQASimpleObjectRegimenConverter("date"));
        dsd.addColumn("TB screening outcome", new TBScreeningAtLastVisitDataDefinition(), "", new TBScreeningConverter("outcome"));
        dsd.addColumn("ART Outcomes", patientOutComes(report), "onDate=${endDate}", new CalculationResultConverter());

    }

    /**
     * Creates the ART `Cohort summary data set
     * @return the data set
     */
    protected DataSetDefinition artCohortSummaryDataset() {

        CohortIndicatorDataSetDefinition cohortDsd = new CohortIndicatorDataSetDefinition();
        cohortDsd.setName("cohortIndicator");

        cohortDsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        String indParams = "startDate=${startDate},endDate=${endDate}";

        EmrReportingUtils.addRow(cohortDsd, "originalArtCohort", "Original ART Cohort", ReportUtils.map(etlArtIndicators.originalArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "transferInArtCohort", "Transfer In ART Cohort", ReportUtils.map(etlArtIndicators.transferINArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "transferOutArtCohort", "Transfer Out ART Cohort", ReportUtils.map(etlArtIndicators.transferOUTArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "originalFirstLineArtCohort", "On Original First Line ART Cohort", ReportUtils.map(etlArtIndicators.onOriginalFirstLineArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "alternateFirstLineArtCohort", "On Alternate First Line ART Cohort", ReportUtils.map(etlArtIndicators.onAlternateFirstLineArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "secondLineArtCohort", "On Second Line ART Cohort", ReportUtils.map(etlArtIndicators.onSecondLineArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "thirdLineArtCohort", "On Third Line ART Cohort", ReportUtils.map(etlArtIndicators.onThirdLineArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "artCohortWithVL", "ART Cohort with VL", ReportUtils.map(etlArtIndicators.withViralLoadResultsArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "artCohortWithSuppresedVL", "ART Cohort with suppressed VL", ReportUtils.map(etlArtIndicators.withSuppressedViralLoadResultsArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "stoppedArtCohort", "ART Cohort stopped ART", ReportUtils.map(etlArtIndicators.stoppedArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "missedAppointmentArtCohort", "Missed Appointment ART", ReportUtils.map(etlArtIndicators.defaulterArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "deadArtCohort", "Dead ART Cohort", ReportUtils.map(etlArtIndicators.deadOnArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));
        EmrReportingUtils.addRow(cohortDsd, "ltfuArtCohort", "Ltfu ART Cohort", ReportUtils.map(etlArtIndicators.ltfuOnArtCohort(), indParams), artCohortAgeDisaggregation, Arrays.asList("01", "02", "03", "04"));

        return cohortDsd;

    }

    private DataDefinition patientOutComes(HybridReportDescriptor descriptor) {
        int months = Integer.parseInt(descriptor.getId().split("\\.")[7]);
        CalculationDataDefinition cd = new CalculationDataDefinition("outcomes", new PatientArtOutComeCalculation());
        cd.addCalculationParameter("outcomePeriod", months);
        cd.addParameter(new Parameter("onDate", "On Date", Date.class));
        return cd;

    }

    private DataDefinition currentCd4Count(HybridReportDescriptor descriptor) {
        CalculationDataDefinition cd = new CalculationDataDefinition("currentCd4", new LastCd4Calculation());
        cd.addParameter(new Parameter("onDate", "On Date", Date.class));
        cd.addCalculationParameter("outcomePeriod", Integer.parseInt(descriptor.getId().split("\\.")[7]));
        return cd;
    }

}
