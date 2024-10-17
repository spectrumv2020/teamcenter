
//Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//==================================================
//Copyright $2014.
//Siemens Product Lifecycle Management Software Inc.
//All Rights Reserved.
//==================================================
//Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

/** 
    @file 

    This file contains the declaration for the Business Object SRB9runtimebo1Impl

*/

#ifndef SAMPLERUNTIMEBO__SRB9RUNTIMEBO1IMPL_HXX
#define SAMPLERUNTIMEBO__SRB9RUNTIMEBO1IMPL_HXX

#include <SRB9sampleruntimebo/SRB9runtimebo1GenImpl.hxx>

#include <SRB9sampleruntimebo/libsrb9sampleruntimebo_exports.h>


namespace sampleruntimebo
{
   class SRB9runtimebo1Impl; 
   class SRB9runtimebo1Delegate;
}
 
class  SRB9SAMPLERUNTIMEBO_API sampleruntimebo::SRB9runtimebo1Impl
           : public sampleruntimebo::SRB9runtimebo1GenImpl 
{
public: 


   /**
    * Getter for an Integer Property
    * @param value - Parameter Value
    * @param isNull - Returns true if the Parameter value is null
    * @return - Status. 0 if successful
    */
    int  getSrb9IntegerPropertyBase( int &value, bool &isNull ) const;

   /**
    * Getter for a string Property
    * @param value - Parameter value
    * @param isNull - Returns true if the Parameter value is null
    * @return - Status. 0 if successful
    */
    int  getSrb9StringPropBase( std::string &value, bool &isNull ) const;

   /**
    * Setter for an Integer Property
    * @param value - Value to be set for the parameter
    * @param isNull - If true, set the parameter value to null
    * @return - Status. 0 if successful
    */
    int  setSrb9IntegerPropertyBase( int value, bool isNull );

   /**
    * Setter for a string Property
    * @param value - Value to be set for the parameter
    * @param isNull - If true, set the parameter value to null
    * @return - Status. 0 if successful
    */
    int  setSrb9StringPropBase( const std::string &value, bool isNull );


   /**
    * desc for setPropertiesFromCreateInpu
    * @param creInput - Description for CreateInput
    * @return - return desc for setPropertiesFromCreateInput
    */
    int  setPropertiesFromCreateInputBase( Teamcenter::CreateInput *creInput );

protected:
    // Constructor for a SRB9runtimebo1
    explicit SRB9runtimebo1Impl( SRB9runtimebo1& busObj );

    // Destructor
    ~SRB9runtimebo1Impl();

private:

    int m_srb9IntegerProp;
    std::string m_srb9StringProp;
    // Default Constructor for the class
    SRB9runtimebo1Impl();
    
    // Private default constructor. We do not want this class instantiated without the business object passed in.
    SRB9runtimebo1Impl( const SRB9runtimebo1Impl& );

    // Copy constructor
    SRB9runtimebo1Impl& operator=( const SRB9runtimebo1Impl& );

    // Method to initialize this Class
    static int initializeClass();

    //static data
    friend class sampleruntimebo::SRB9runtimebo1Delegate;

};

#include <SRB9sampleruntimebo/libsrb9sampleruntimebo_undef.h>
#endif // SAMPLERUNTIMEBO__SRB9RUNTIMEBO1IMPL_HXX
