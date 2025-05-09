/*
 * Copyright (C) 2013-2021 Apple Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY APPLE INC. ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL APPLE INC. OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#pragma once

#include "JITCompilationMode.h"
#include <wtf/HashMap.h>

namespace JSC {

class JSCell;

class JITCompilationKey {
public:
    JITCompilationKey()
        : m_codeBlock(nullptr)
        , m_mode(JITCompilationMode::InvalidCompilation)
    {
    }

    JITCompilationKey(WTF::HashTableDeletedValueType)
        : m_codeBlock(nullptr)
        , m_mode(JITCompilationMode::DFG)
    {
    }

    JITCompilationKey(JSCell* profiledBlock, JITCompilationMode mode)
        : m_codeBlock(profiledBlock)
        , m_mode(mode)
    {
    }

    explicit operator bool() const
    {
        return m_codeBlock || m_mode != JITCompilationMode::InvalidCompilation;
    }

    bool isHashTableDeletedValue() const
    {
        return !m_codeBlock && m_mode != JITCompilationMode::InvalidCompilation;
    }

    JITCompilationMode mode() const { return m_mode; }

    friend bool operator==(const JITCompilationKey&, const JITCompilationKey&) = default;

    unsigned hash() const
    {
        return WTF::pairIntHash(WTF::PtrHash<JSCell*>::hash(m_codeBlock), static_cast<std::underlying_type<JITCompilationMode>::type>(m_mode));
    }

    void dump(PrintStream&) const;

private:
    // Either CodeBlock* or UnlinkedCodeBlock* for basleline JIT.
    JSCell* m_codeBlock;
    JITCompilationMode m_mode;
};

struct JITCompilationKeyHash {
    static unsigned hash(const JITCompilationKey& key) { return key.hash(); }
    static bool equal(const JITCompilationKey& a, const JITCompilationKey& b) { return a == b; }
    static constexpr bool safeToCompareToEmptyOrDeleted = true;
};

} // namespace JSC

namespace WTF {

template<typename T> struct DefaultHash;
template<> struct DefaultHash<JSC::JITCompilationKey> : JSC::JITCompilationKeyHash { };

template<typename T> struct HashTraits;
template<> struct HashTraits<JSC::JITCompilationKey> : SimpleClassHashTraits<JSC::JITCompilationKey> { };

} // namespace WTF
