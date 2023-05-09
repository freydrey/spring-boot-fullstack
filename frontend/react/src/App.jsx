import { Wrap
    ,WrapItem
    ,Text
    ,Spinner} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/sidebar.jsx";
import React, {useEffect, useState} from "react";
import {getCustomers} from "./services/client.jsx";
import CardWithImage from "./components/Card.jsx";

const App = () => {

    const[customers, setCustomers] = useState([]);
    const[loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        setTimeout(()=>{
            getCustomers().then(res => {
                setCustomers(res.data);
            }).catch(err => {
                console.log(err);
            }).finally(() => {
                setLoading(false);
            })
        },3000)
    },[])

    if(loading){
        return(
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if(customers.length <= 0){
        return(<SidebarWithHeader>
            <Text>No Customers Available</Text>
        </SidebarWithHeader>);
    }

    return (
        <SidebarWithHeader>
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer, index)=>(
                    <WrapItem key={index}>
                        <CardWithImage {...customer} />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;





/*import UserProfile from "./UserProfile.jsx";
import { useState, useEffect } from "react";

const users = [
    {
        name: "JAmilla",
        age: 23,
        gender: "FEMALE"
    },
    {
        name: "Alex",
        age: 23,
        gender: "MALE"
    },
    {
        name: "Paul",
        age: 23,
        gender: "MALE"
    }

]


const UserProfiles = ({ users }) => (
    <div>
        {users.map((user, index) => (
            <UserProfile
                key={index}
                name={user.name}
                age={user.age}
                gender={user.gender}
                imageNumber={index}
            />
        ))}
    </div>

)

function App() {

    const [counter, setCounter] = useState(0);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true);
        setTimeout(() => {
            setIsLoading(false);
        }, 4000)
        return() => {
            console.log("clean up function")
        }
    }, [])

    if(isLoading){
        return "Loading...";
    }

    return (
        <div>
            <button
                onClick={() => setCounter(prevCounter => prevCounter + 1)}>
                Increment counter</button>
            <h1>{counter}</h1>
            <UserProfiles users={users}/>
        </div>

    )
}

export default App*/
