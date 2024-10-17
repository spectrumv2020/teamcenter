//Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//==================================================
//Copyright $2014.
//Siemens Product Lifecycle Management Software Inc.
//All Rights Reserved.
//==================================================
//Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.

/** 
    @file 

    This file contains the implementation for the Business Object SRB9runtimebo1

*/

#include <SRB9sampleruntimebo/SRB9runtimebo1.hxx>
#include <SRB9sampleruntimebo/SRB9runtimebo1Impl.hxx>
#include <fclasses/tc_string.h>
#include <tc/tc.h>

using namespace sampleruntimebo; 

//----------------------------------------------------------------------------------
// SRB9runtimebo1Impl::SRB9runtimebo1Impl(SRB9runtimebo1& busObj)
// Constructor for the class
//----------------------------------------------------------------------------------
SRB9runtimebo1Impl::SRB9runtimebo1Impl( SRB9runtimebo1& busObj )
   : SRB9runtimebo1GenImpl( busObj )
{
    m_srb9IntegerProp = 0;
}

//----------------------------------------------------------------------------------
// SRB9runtimebo1Impl::~SRB9runtimebo1Impl()
// Destructor for the class
//----------------------------------------------------------------------------------
SRB9runtimebo1Impl::~SRB9runtimebo1Impl()
{
}
 
//----------------------------------------------------------------------------------
// SRB9runtimebo1Impl::initializeClass
// This method is used to initialize this Class
//----------------------------------------------------------------------------------
int SRB9runtimebo1Impl::initializeClass()
{
    int ifail = ITK_ok;
    static bool initialized = false;

    if( !initialized )
    {
        ifail = SRB9runtimebo1GenImpl::initializeClass( );
        if ( ifail == ITK_ok )
        {
            initialized = true;
        }
    }
    return ifail;
}


/**
 * Getter for an Integer Property
 * @param value - Parameter Value
 * @param isNull - Returns true if the Parameter value is null
 * @return - Status. 0 if successful
 */
int  SRB9runtimebo1Impl::getSrb9IntegerPropertyBase( int & value, bool & /*isNull*/ ) const
{
    value = m_srb9IntegerProp;
    return ITK_ok;    
}

/**
 * Getter for a string Property
 * @param value - Parameter value
 * @param isNull - Returns true if the Parameter value is null
 * @return - Status. 0 if successful
 */
int  SRB9runtimebo1Impl::getSrb9StringPropBase( std::string & value, bool & /*isNull*/ ) const
{
    value = m_srb9StringProp;
    return ITK_ok;
}

/**
 * Setter for an Integer Property
 * @param value - Value to be set for the parameter
 * @param isNull - If true, set the parameter value to null
 * @return - Status. 0 if successful
 */
int  SRB9runtimebo1Impl::setSrb9IntegerPropertyBase( int  value, bool  /*isNull*/ )
{
    m_srb9IntegerProp = value;
    return ITK_ok;
}

/**
 * Setter for a string Property
 * @param value - Value to be set for the parameter
 * @param isNull - If true, set the parameter value to null
 * @return - Status. 0 if successful
 */
int  SRB9runtimebo1Impl::setSrb9StringPropBase( const std::string & value, bool  /*isNull*/ )
{
    m_srb9StringProp = value;
    return ITK_ok;
}


/**
 * desc for setPropertiesFromCreateInpu
 * @param creInput - Description for CreateInput
 * @return - return desc for setPropertiesFromCreateInput
 */
int  SRB9runtimebo1Impl::setPropertiesFromCreateInputBase( Teamcenter::CreateInput * creInput )
{
    int ifail = ITK_ok;

    bool isNull = false;
    std::string srb9StringPropVal;
    creInput->getString("srb9StringProp",srb9StringPropVal,isNull);
    ifail = setString( "srb9StringProp", srb9StringPropVal, isNull );
    if ( ifail != ITK_ok )
    {
        return ifail;
    }

    int intValue=0;
    creInput->getInt( "srb9IntegerProperty",intValue,isNull );
    ifail = setInt( "srb9IntegerProperty", intValue, isNull );
    if ( ifail != ITK_ok )
    {
        return ifail;
    }
    
    return ifail;
}
