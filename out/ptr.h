/*
 * Object-Oriented Programming
 * Copyright (C) 2011 Robert Grimm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */

#pragma once

#include <iostream>
#include <cstring>

#if 0
#define TRACE(addr) \
  std::cout << __FUNCTION__ << ":" << __LINE__ << ":" << addr << std::endl
#else
#define TRACE(addr)
#endif

namespace __rt {

  template<typename T>
  class Ptr {
    T* addr;
    size_t* counter;
    
  public:
    typedef T value_t;

    inline Ptr(T* addr = 0) : addr(addr), counter(new size_t(1)) {
      TRACE(addr);
    }

    inline Ptr(const Ptr& other) : addr(other.addr), counter(other.counter) {
      TRACE(addr);
      ++(*counter);
    }

    inline ~Ptr() {
      TRACE(addr);
      if (0 == --(*counter)) {
        if (0 != addr) addr->__vptr->__delete(addr);
        delete counter;
      }
    }

    inline Ptr& operator=(const Ptr& right) {
      TRACE(addr);
      if (addr != right.addr) {
        if (0 == --(*counter)) {
          if (0 != addr) addr->__vptr->__delete(addr);
          delete counter;
        }
        addr = right.addr;
        counter = right.counter;
        ++(*counter);
      }
      return *this;
    }

    inline T& operator*()  const { TRACE(addr); return *addr; }
    inline T* operator->() const { TRACE(addr); return addr;  }
    inline T* raw()        const { TRACE(addr); return addr;  }

    template<typename U>
    friend class Ptr;

    template<typename U>
    inline Ptr(const Ptr<U>& other)
    : addr((T*)other.addr), counter(other.counter) {
      TRACE(addr);
      ++(*counter);
    }

    template<typename U>
    inline bool operator==(const Ptr<U>& other) const {
      return addr == (T*)other.addr;
    }
    
    template<typename U>
    inline bool operator!=(const Ptr<U>& other) const {
      return addr != (T*)other.addr;
    }

  };

}
