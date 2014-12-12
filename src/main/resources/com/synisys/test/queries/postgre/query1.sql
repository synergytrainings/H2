SELECT     MIN(CreatedUser) AS A2_II_CreatedUser, MIN(NameCode) AS A2_II_Name_IIs, COALESCE(II_IIs, 1) AS II_IIs, 0 AS IREPORTBRANCHID, SUM(1) 
                      AS TempColumn
FROM         (SELECT     IIID, IICount, ActiveIICount, IICode, ApplicationDate, ApplicationStatusID, PersonID, ElectoralID, NationalIDCard, NameCode, IIName_ENG, IIName_NAT, 
                                              IIName_POR, SocialAnimatorID, PaymentStatusID, BeneficiaryTypeID, DisabilityTypeID, DisabilityLevelID, SocialAnimator_ENG, SocialAnimator_NAT, 
                                              SocialAnimator_POR, DateUpdated, DateCreated, UpdatedUser, CreatedUser, Statusname_ENG, Statusname_NAT, Statusname_POR, 
                                              Beneficiaryname_ENG, Beneficiaryname_NAT, Beneficiaryname_POR, GenderName_ENG, GenderName_NAT, GenderName_POR, 
                                              BenefitingProgram_ENG, BenefitingProgram_NAT, BenefitingProgram_POR, NationalityName_ENG, NationalityName_NAT, NationalityName_POR, 
                                              MaritalStatusName_ENG, MaritalStatusName_NAT, MaritalStatusName_POR, Religion_ENG, Religion_NAT, Religion_POR, MonthlyAmount_USD, Phone, 
                                              DateVerified, OwnerID, ReligionID, StateProgramID, ApplicationStatusID AS II_ApplicationStatuses, BeneficiaryTypeID AS II_BeneficiaryTypes, 
                                              DisabilityLevelID AS II_DisabilityLevels, DisabilityTypeID AS II_DisabilityTypes, IIID AS II_IIs, SocialAnimatorID AS II_SocialAnimators, 
                                              StateProgramID AS II_StatePrograms
                       FROM          View_IIs) AS View_IIs
GROUP BY II_IIs